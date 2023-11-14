package kopo.poly.controller;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import kopo.poly.dto.*;
import kopo.poly.repository.entity.EventEntity;
import kopo.poly.repository.entity.JoinEntity;
import kopo.poly.repository.entity.TagEntity;
import kopo.poly.service.IApiService;
import kopo.poly.service.IAwsS3Service;
import kopo.poly.service.IEventService;
import kopo.poly.util.CmmUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.CookieStore;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequestMapping(value = "/event")
@RequiredArgsConstructor
@Controller
public class EventController {
    private final IApiService ApiService;
    private final IAwsS3Service awsS3Service;
    private final IEventService eventService;

    private final MainController mainController;
    @GetMapping(value = "api_event")
    public String api_event() throws Exception{
        log.info(this.getClass().getName() + ".api_event Start!");
        log.info(this.getClass().getName() + ".api_event End!");
        return "event/api_event";
    }
    @GetMapping(value = "eventJoin")
    public String eventJoin(@RequestParam("title")String title,@RequestParam("price")String price,ModelMap model) throws Exception{
        log.info(this.getClass().getName() + "join1 Start!");
        model.addAttribute("title",title);
        model.addAttribute("price",price);
        log.info(this.getClass().getName() + "join1 End!");
        return "event/eventJoin";
    }
    @PostMapping(value = "Join")
    @ResponseBody
    public MsgDTO join(@RequestParam("title")String title,@RequestParam("cellphoneNo")String phone, @RequestParam("userName")String name,HttpServletRequest request, HttpSession session,ModelMap model) throws Exception{
        log.info(this.getClass().getName() + "join Start!");

        int res = 0;
        String msg = "";
        MsgDTO dto = null;


        JoinDTO pDTO = null;

        try {

            String userId = (String) session.getAttribute("SS_USER_ID"); //아이디


            log.info("userId : " + userId);
            log.info("userName : " + name);

            pDTO = new JoinDTO();
            pDTO.setCellphoneNo(phone);
            pDTO.setUserid(userId);
            pDTO.setName(name);
            pDTO.setTitle(title);



            res = eventService.join(pDTO);

            log.info("회원가입 결과(res) : " + res);

            if (res == 1) {
                msg = "신청 완료";


            } else if (res == 2) {
                msg = "이미 신청하셨습니다.";

            } else {
                msg = "오류로 인해 신청이 실패하였습니다.";

            }

        } catch (Exception e) {

            msg = "실패하였습니다. : " + e;
            log.info(e.toString());
            e.printStackTrace();

        } finally {

            dto = new MsgDTO();
            dto.setResult(res);
            dto.setMsg(msg);


        }
        log.info(this.getClass().getName() + "join End!");
        return dto;
    }
    @GetMapping(value = "Join2")
    public String join2(HttpSession session,ModelMap model) throws Exception{
        log.info(this.getClass().getName() + "join Start!");

        int res = 0;
        String msg = "";
        String url="";

        JoinDTO pDTO = null;

        try {

            String userId = CmmUtil.nvl((String) session.getAttribute("SS_USER_ID")); //아이디
            String name = CmmUtil.nvl((String) session.getAttribute("name")); //아이디
            String phone = CmmUtil.nvl((String) session.getAttribute("phone")); //아이디
            String title = CmmUtil.nvl((String) session.getAttribute("title")); //아이디


            log.info("userId : " + userId);
            log.info("userName : " + name);

            pDTO = new JoinDTO();
            pDTO.setCellphoneNo(phone);
            pDTO.setUserid(userId);
            pDTO.setName(name);
            pDTO.setTitle(title);

            session.setAttribute("name", ""); // 세션 값 빈값으로 변경
            session.removeAttribute("name"); // 세션 값 지우기
            session.setAttribute("phone", ""); // 세션 값 빈값으로 변경
            session.removeAttribute("phone"); // 세션 값 지우기
            session.setAttribute("title", ""); // 세션 값 빈값으로 변경
            session.removeAttribute("title"); // 세션 값 지우기


            // 회원가입 서비스 호출하여 결과 받기
            res = eventService.join(pDTO);

            log.info("회원가입 결과(res) : " + res);

            if (res == 1) {
                msg = "신청 완료";
                url="/index";

                //추후 회원가입 입력화면에서 ajax를 활용해서 아이디 중복, 이메일 중복을 체크하길 바람
            } else if (res == 2) {
                msg = "이미 신청하셨습니다.";
                url="/index";

            } else {
                msg = "오류로 인해 신청이 실패하였습니다.";
                url="/index";

            }

        } catch (Exception e) {
            //저장이 실패되면 사용자에게 보여줄 메시지
            msg = "실패하였습니다. : " + e;
            log.info(e.toString());
            e.printStackTrace();

        } finally {
            // 결과 메시지 전달하기
            model.addAttribute("msg",msg);
            model.addAttribute("url",url);


        }
        log.info(this.getClass().getName() + "join End!");
        return "success";
    }

    @GetMapping(value = "event")
    public String event() throws Exception{
        log.info(this.getClass().getName() + ".event Start!");
        log.info(this.getClass().getName() + ".event End!");
        return "event/event";
    }
    @GetMapping(value = "event2")
    public String event2() throws Exception{
        log.info(this.getClass().getName() + ".event2 Start!");
        log.info(this.getClass().getName() + ".event2 End!");
        return "event/event2";
    }
    @GetMapping(value = "searchTag")
    public String searchTag(@RequestParam("tag")String tag,ModelMap model) throws Exception{
        log.info(this.getClass().getName() + ".searchTag Start!");
        model.addAttribute("tag",tag);
        log.info(this.getClass().getName() + ".searchTag End!");
        return "event/searchTag";
    }
    @PostMapping(value = "searchTag2")
    @ResponseBody
    public List<EventEntity> searchTag2(@RequestParam("tag")String tag,ModelMap model) throws Exception{
        log.info(this.getClass().getName() + ".searchTag2 Start!");
        model.addAttribute("tag",tag);
        List<EventEntity> sList = new ArrayList<>();
        sList = eventService.searchTag(tag);
        log.info(this.getClass().getName() + ".searchTag2 End!");
        return sList;
    }
    @GetMapping(value = "eventSearch")
    public String eventSearch(@RequestParam("title")String title,@RequestParam("city")String city,@RequestParam("category")String category,ModelMap model) throws Exception{
        log.info(this.getClass().getName() + ".eventSearch Start!");
        model.addAttribute("title",title);
        model.addAttribute("city",city);
        model.addAttribute("category",category);
        log.info(this.getClass().getName() + ".eventSearch End!");
        return "event/eventSearch";
    }
    @PostMapping(value = "eventSearch2")
    @ResponseBody
    public List<EventDTO> eventSearch2(HttpServletRequest request,ModelMap model) throws Exception{
        log.info(this.getClass().getName() + ".eventSearch2 Start!");
        String title =request.getParameter("title");
        String city =request.getParameter("city");
        String category =request.getParameter("category");
        SearchDTO sDTO = new SearchDTO();
        sDTO.setTitle(title);
        sDTO.setCity(city);
        sDTO.setCategory(category);
        List<EventEntity> rList=eventService.searchEvent(sDTO);
        List<EventDTO> nList = new ArrayList<EventDTO>();
        for (EventEntity s : rList){
            EventDTO temp = new EventDTO();
            temp.setTitle(s.getTitle());
            temp.setFirstimage(s.getFirstimage());
            temp.setPrice(s.getPrice());
            temp.setDetail(s.getDetail());
            nList.add(temp);
        }
        List<EventDTO> lList=mainController.api();
        List<EventDTO> sList = new ArrayList<EventDTO>();
        for (EventDTO s : lList){
            if(!title.equals("")){
                if(!city.equals("선택안함")){
                    if(s.getTitle().contains(title)&&s.getAddr().contains(city)){
                        sList.add(s);
                    }
                }else {
                    if(s.getTitle().contains(title)){
                        sList.add(s);
                    }
                }
            }

        }
        Iterable<EventDTO> joinedIterable = Iterables.unmodifiableIterable(
                Iterables.concat(nList, sList));
        List<EventDTO> jList = Lists.newArrayList(joinedIterable);
        for(EventEntity s : rList){
            log.info("타이틀 : "+s.getTitle());
        }
        log.info(this.getClass().getName() + ".eventSearch2 End!");
        return jList;
    }
    @GetMapping(value = "event3")
    public String event3() throws Exception{
        log.info(this.getClass().getName() + ".event3 Start!");
        log.info(this.getClass().getName() + ".event3 End!");
        return "event/event3";
    }
    @GetMapping(value = "eventpage")
    public String eventpage(@RequestParam("title")String title,Model model) throws Exception{
        log.info(this.getClass().getName() + "이벤트 페이지 상세보기1 Start!");
        String result = ApiService.getEvent();
        log.info("데이터: "+result);
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject;
        JSONArray parse_itemlist=null;
        try{
            jsonObject = (JSONObject) jsonParser.parse(result);
            JSONObject parse_response = (JSONObject) jsonObject.get("response");
            JSONObject parse_body = (JSONObject) parse_response.get("body");
            JSONObject parse_items = (JSONObject) parse_body.get("items");
            parse_itemlist = (JSONArray) parse_items.get("item");
            EventDTO eDTO = new EventDTO();
            for(int i=0; i<parse_itemlist.size(); i++){
                JSONObject event = (JSONObject) parse_itemlist.get(i);
                log.info("타이틀 : "+(String)event.get("title"));
                if (title.equals((String)event.get("title"))){
                    model.addAttribute("title",event.get("title"));
                    model.addAttribute("addr1",event.get("addr1"));
                    model.addAttribute("eventstartdate",event.get("eventstartdate"));
                    model.addAttribute("eventenddate",event.get("eventenddate"));
                    model.addAttribute("firstimage",event.get("firstimage"));
                    model.addAttribute("firstimage2",event.get("firstimage2"));
                    model.addAttribute("mapx",event.get("mapx"));
                    model.addAttribute("mapy",event.get("mapy"));
                    model.addAttribute("tel",event.get("tel"));

                }
            }



        }catch (Exception e){
            e.printStackTrace();
        }
        log.info(this.getClass().getName() + ".eventpage End!");
        return "event/eventpage";
    }
    @GetMapping(value = "eventpage2")
    public String eventpage2(@RequestParam("title")String title,ModelMap model,HttpServletRequest request,
                             HttpServletResponse response,HttpSession session) throws Exception{
        log.info(this.getClass().getName() + "이벤트 페이지 상세보기2 Start!");
        EventDTO eventDTO = new EventDTO();
        String id = (String) session.getAttribute("SS_USER_ID");
        String utitle = URLEncoder.encode(title,"utf-8");
        log.info(title);
        if(id!=null) {
            /* 조회수 로직 */
            Cookie oldCookie = null;
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals(utitle)) {
                        oldCookie = cookie;
                    }
                }
            }

            if (oldCookie != null) {
                log.info("올드쿠키 실행 : "+oldCookie.getValue().contains("["+ utitle +"]"));
                if (!oldCookie.getValue().contains("[" + utitle + "]")) {
                    eventDTO = eventService.findEvent(id,title, 1);
                    oldCookie.setValue(oldCookie.getValue() + "_[" + utitle + "]");
                    oldCookie.setPath("/");
                    oldCookie.setMaxAge(60 * 60 * 24);                            // 쿠키 시간
                    response.addCookie(oldCookie);
                }else {
                    eventDTO = eventService.findEvent(id,title, 1);
                }
                log.info("조회수 쿠키 생성되어있음");
            } else {
                eventDTO = eventService.findEvent(id,title, 0);
                Cookie newCookie = new Cookie(utitle, "[" + utitle + "]");
                newCookie.setPath("/");
                newCookie.setMaxAge(60 * 60 * 24);                                // 쿠키 시간
                response.addCookie(newCookie);
                log.info("조회수 쿠키 생성");
            }
        }else{
            eventDTO = eventService.findEvent(id,title, 1);
        }
        log.info("title : "+eventDTO.getTitle());
        log.info("image : "+eventDTO.getFirstimage());


        model.addAttribute("title",eventDTO.getTitle());
        model.addAttribute("image",eventDTO.getFirstimage());
        model.addAttribute("addr",eventDTO.getAddr());
        model.addAttribute("detail",eventDTO.getDetail());
        model.addAttribute("channel",eventDTO.getChannel());
        model.addAttribute("price",eventDTO.getPrice());
        model.addAttribute("readCnt",eventDTO.getReadCnt());
        LikeDTO lDTO=eventService.countLike(title);
        model.addAttribute("good",lDTO.getGood());
        model.addAttribute("hate",lDTO.getHate());

        log.info(this.getClass().getName() + ".eventpage End!");
        return "event/eventpage2";
    }
    @PostMapping("like")
    @ResponseBody
    public LikeDTO like(HttpServletRequest request,HttpSession session,@RequestParam("title")String title)throws Exception{
        log.info("좋아요 시작");
        String user = (String) session.getAttribute("SS_USER_ID");
        eventService.like(user,title,1);
        LikeDTO lDTO=eventService.countLike(title);
        log.info("좋아요 끝");
        return lDTO;
    }
    @PostMapping("hate")
    @ResponseBody
    public LikeDTO hate(HttpServletRequest request,HttpSession session,@RequestParam("title")String title) throws Exception {
        log.info("싫어요 시작");
        String user = (String) session.getAttribute("SS_USER_ID");
        eventService.like(user,title,-1);
        LikeDTO lDTO=eventService.countLike(title);
        log.info("싫어요 끝");
        return lDTO;
    }

    @GetMapping(value = "tag")
    @ResponseBody
    public List<TagEntity> tag( @RequestParam("title")String title) {
        log.info("태그 시작");
        List<TagEntity> rList=eventService.tag(title);
        log.info("태그 끝");
        return rList;
    }

    @PostMapping("delete")
    @ResponseBody
    public List<EventEntity> delete(HttpServletRequest request){
        log.info("delete 시작");
        log.info(request.getParameter("title"));
        List<EventEntity> rList = eventService.delete(request.getParameter("title"));
        log.info("delete 끝");
        return rList;
    }

    @GetMapping(value = "findevent")
    @ResponseBody
    public List<EventEntity> findevent(@RequestParam(value = "page",defaultValue = "0")int page, ModelAndView model) throws Exception {
        log.info("이벤트 시작");
        int size=5;
        List<EventEntity> pageEvent = new ArrayList<EventEntity>();
        Pageable paging = PageRequest.of(page, size);
        model.addObject("page",page);
        model.addObject("size",size);
        pageEvent = eventService.findAllEvent(paging);
        int total = eventService.countAll();
        model.addObject("total",total);
        log.info(String.valueOf(page));
        log.info(String.valueOf(size));
        log.info(String.valueOf(total));
        log.info("이벤트 끝");
        return pageEvent;
    }
    @GetMapping("paging")
    @ResponseBody
    public PageDTO paging(@RequestParam(value = "page",defaultValue = "0")int page) throws Exception {
        log.info("이벤트 시작");
        int size=5;
        PageDTO pDTO = new PageDTO();
        int total = eventService.countAll();
        log.info(String.valueOf(page));
        log.info(String.valueOf(size));
        log.info(String.valueOf(total));
        pDTO.setPage(page);
        pDTO.setTotal(total);
        pDTO.setSize(size);
        log.info("이벤트 끝");
        return pDTO;
    }
    @GetMapping(value = "findevent2")
    @ResponseBody
    public List<EventEntity> findevent2(HttpSession session) throws Exception {
        List<EventEntity> rList = eventService.findEvent2((String)session.getAttribute("channel"));
        return rList;
    }
    @GetMapping(value = "findevent3")
    @ResponseBody
    public List<JoinEntity> findevent3(HttpSession session) throws Exception {
        List<JoinEntity> rList = eventService.findEvent3((String)session.getAttribute("SS_USER_ID"));
        return rList;
    }
    @GetMapping(value = "findevent4")
    @ResponseBody
    public List<EventEntity> findevent4(@RequestParam(value = "page",defaultValue = "0")int page,HttpSession session) throws Exception {
        int size=5;
        List<EventEntity> pageEvent = new ArrayList<EventEntity>();
        Pageable paging = PageRequest.of(page, size);
        pageEvent = eventService.findEvent4("오프라인",paging);
        return pageEvent;
    }
    @GetMapping("paging2")
    @ResponseBody
    public PageDTO paging2(@RequestParam(value = "page",defaultValue = "0")int page) throws Exception {
        log.info("이벤트 시작");
        int size=5;
        PageDTO pDTO = new PageDTO();
        int total = eventService.countAll2("오프라인");
        log.info(String.valueOf(page));
        log.info(String.valueOf(size));
        log.info(String.valueOf(total));
        pDTO.setPage(page);
        pDTO.setTotal(total);
        pDTO.setSize(size);
        log.info("이벤트 끝");
        return pDTO;
    }
    @GetMapping("paging3")
    @ResponseBody
    public PageDTO paging3(@RequestParam(value = "page",defaultValue = "0")int page) throws Exception {
        log.info("이벤트 시작");
        int size=5;
        PageDTO pDTO = new PageDTO();
        int total = eventService.countAll3("온라인");
        log.info(String.valueOf(page));
        log.info(String.valueOf(size));
        log.info(String.valueOf(total));
        pDTO.setPage(page);
        pDTO.setTotal(total);
        pDTO.setSize(size);
        log.info("이벤트 끝");
        return pDTO;
    }
    @GetMapping(value = "findevent5")
    @ResponseBody
    public List<EventEntity> findevent5(@RequestParam(value = "page",defaultValue = "0")int page,HttpSession session) throws Exception {
        int size=5;
        List<EventEntity> pageEvent = new ArrayList<EventEntity>();
        Pageable paging = PageRequest.of(page, size);
        pageEvent = eventService.findEvent4("온라인",paging);
        return pageEvent;
    }
    @GetMapping(value = "findTitle")
    @ResponseBody
    public List<JoinEntity> findTitle(@RequestParam("title")String title) throws Exception {
        List<JoinEntity> rList = eventService.findTitle(title);
        return rList;
    }
    @GetMapping(value = "channel")
    public String channel() throws Exception{
        log.info(this.getClass().getName() + ".event_channel Start!");
        log.info(this.getClass().getName() + ".event_channel End!");
        return "event/event_channel";
    }
    @GetMapping(value = "channel2")
    public String channel2() throws Exception{
        log.info(this.getClass().getName() + ".event_channel Start!");
        log.info(this.getClass().getName() + ".event_channel End!");
        return "event/event_channel2";
    }
    @GetMapping(value = "eventcreate")
    public String eventcreate() throws Exception{
        log.info(this.getClass().getName() + ".eventcreate Start!");
        log.info(this.getClass().getName() + ".eventcreate End!");
        return "event/eventcreate";
    }
    @ApiOperation(value = "Amazon S3에 이미지 업로드", notes = "Amazon S3에 이미지 업로드 ")
    @PostMapping("/create")
    public String ChannelCreate(@ApiParam(value="img 파일들(여러 파일 업로드 가능)", required = true) @RequestPart List<MultipartFile> multipartFile, HttpServletRequest request, HttpSession session, ModelMap model) {
        log.info("awsS3 Upload Start");

        MsgDTO dto = null;
        ChannelDTO pDTO = null;
        int res = 0;
        String msg = "";
        String url = "";

        log.info("파일1 : "+multipartFile);
        try {
            List<String> rList = awsS3Service.uploadImage(multipartFile);
            log.info(rList.get(0));
            String file = "https://poly-event.s3.ap-northeast-2.amazonaws.com/"+rList.get(0);
            log.info("파일명 : "+file);
            String channel = request.getParameter("name");
            log.info("채널명 : "+channel);
            String UserId = (String) session.getAttribute("SS_USER_ID");
            log.info("유저 아이디 : "+UserId);

            pDTO = new ChannelDTO();
            pDTO.setChannel(channel);
            pDTO.setUserId(UserId);
            pDTO.setFile(file);

            res = eventService.insertChannel(pDTO);
            if (res == 1) { //로그인 성공

                msg = "채널 등록을 성공했습니다.";
                url = "/event/channelpage";

            } else {
                msg = "채널 등록에 실패했습니다.";
                url = "/event/channel";
            }


        } catch (Exception e) {
            //저장이 실패되면 사용자에게 보여줄 메시지
            msg = "시스템 문제로 채널 등록이 실패했습니다.";
            url="/event/channel";
            res = 2;
            log.info(e.toString());
            e.printStackTrace();

        } finally {
            // 결과 메시지 전달하기
            model.addAttribute("msg",msg);
            model.addAttribute("url",url);
        }
        log.info("awsS3 Upload End");
        return "success";
    }
    @ApiOperation(value = "Amazon S3에 이미지 업로드", notes = "Amazon S3에 이미지 업로드 ")
    @PostMapping("/update")
    public String ChannelUpdate(@ApiParam(value="img 파일들(여러 파일 업로드 가능)", required = true) @RequestPart List<MultipartFile> multipartFile, HttpServletRequest request, HttpSession session, ModelMap model) {
        log.info("awsS3 Upload Start");

        MsgDTO dto = null;
        ChannelDTO pDTO = null;
        int res = 0;
        String msg = "";
        String url = "";

        log.info("파일1 : "+multipartFile);
        try {
            List<String> rList = awsS3Service.uploadImage(multipartFile);
            log.info(rList.get(0));
            String file = "https://poly-event.s3.ap-northeast-2.amazonaws.com/"+rList.get(0);
            log.info("파일명 : "+file);
            String channel = request.getParameter("name");
            log.info("채널명 : "+channel);
            String UserId = (String) session.getAttribute("SS_USER_ID");
            log.info("유저 아이디 : "+UserId);

            pDTO = new ChannelDTO();
            pDTO.setChannel(channel);
            pDTO.setUserId(UserId);
            pDTO.setFile(file);

            res = eventService.updateChannel(pDTO);
            if (res == 1) {

                msg = "채널 수정을 성공했습니다.";
                url = "/event/channelpage";

            } else {
                msg = "채널 수정에 실패했습니다.";
                url = "/event/channel";
            }


        } catch (Exception e) {
            //저장이 실패되면 사용자에게 보여줄 메시지
            msg = "시스템 문제로 채널 수정이 실패했습니다.";
            url="/event/channel";
            res = 2;
            log.info(e.toString());
            e.printStackTrace();

        } finally {
            // 결과 메시지 전달하기
            model.addAttribute("msg",msg);
            model.addAttribute("url",url);
        }
        log.info("awsS3 Upload End");
        return "success";
    }
    @GetMapping(value = "channelpage")
    public String channelpage(HttpSession session,ModelMap model) throws Exception{
        log.info(this.getClass().getName() + ".channelpage Start!");
        ChannelDTO rDTO = new ChannelDTO();
        rDTO = eventService.channelFinder((String) session.getAttribute("SS_USER_ID"));
        log.info(rDTO.getChannel());
        log.info(rDTO.getFile());
        log.info(rDTO.getUserId());
        model.addAttribute("channel",rDTO.getChannel());
        model.addAttribute("file",rDTO.getFile());
        model.addAttribute("userid",rDTO.getUserId());
        session.setAttribute("channel",rDTO.getChannel());
        log.info(this.getClass().getName() + ".channelpage End!");
        return "event/channelpage";
    }
    @ApiOperation(value = "Amazon S3에 이미지 업로드", notes = "Amazon S3에 이미지 업로드 ")
    @PostMapping("/event_create")
    public String event_create(@ApiParam(value="img 파일들(여러 파일 업로드 가능)", required = true) @RequestPart List<MultipartFile> multipartFile, HttpServletRequest request, HttpSession session, ModelMap model) {
        log.info("event_create Start");

        MsgDTO dto = null;
        EventDTO pDTO = null;
        int res = 0;
        String msg = "";
        String url = "";

        log.info("파일1 : "+multipartFile);
        try {
            List<String> rList = awsS3Service.uploadImage(multipartFile);
            log.info(rList.get(0));
            String file = "https://poly-event.s3.ap-northeast-2.amazonaws.com/"+rList.get(0);
            log.info("파일명 : "+file);
            String channel = (String) session.getAttribute("channel");
            log.info("채널명 : "+channel);
            String detail = request.getParameter("detail");
            log.info("이벤트 설명 : "+detail);
            String title = request.getParameter("title");
            log.info("이베트 제목 : "+title);
            String price = request.getParameter("price");
            log.info("가격 : "+price);
            String addr = request.getParameter("addr");
            log.info("주소 : "+addr);
            String category = request.getParameter("category");
            log.info("카테고리 : "+category);
            String tag = request.getParameter("tag");
            log.info("태그 : "+tag);

            pDTO = new EventDTO();
            pDTO.setFirstimage(file);
            pDTO.setAddr(addr);
            pDTO.setTitle(title);
            pDTO.setChannel(channel);
            pDTO.setDetail(detail);
            pDTO.setPrice(Integer.parseInt(price));
            pDTO.setCategory(category);
            pDTO.setTag(tag);

            res = eventService.insertEvent(pDTO);
            if (res == 1) { //로그인 성공

                msg = "이벤트 등록을 성공했습니다.";
                url = "/event/channelpage";

            } else {
                msg = "이벤트 등록에 실패했습니다.";
                url = "/event/channel";
            }


        } catch (Exception e) {
            //저장이 실패되면 사용자에게 보여줄 메시지
            msg = "시스템 문제로 이벤트 등록이 실패했습니다.";
            url="/event/channel";
            res = 2;
            log.info(e.toString());
            e.printStackTrace();

        } finally {
            // 결과 메시지 전달하기
            model.addAttribute("msg",msg);
            model.addAttribute("url",url);
        }
        log.info("event_create End");
        return "success";
    }

}
