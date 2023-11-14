package kopo.poly.controller;

import kopo.poly.dto.ChannelDTO;
import kopo.poly.dto.MsgDTO;
import kopo.poly.dto.UserInfoDTO;
import kopo.poly.service.IEventService;
import kopo.poly.service.IUserInfoService;
import kopo.poly.util.CmmUtil;
import kopo.poly.util.EncryptUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

@Slf4j
@RequestMapping(value = "/user")
@RequiredArgsConstructor
@Controller
public class UserInfoController {

    // @RequiredArgsConstructor 를 통해 메모리에 올라간 서비스 객체를 Controller에서 사용할 수 있게 주입함
    private final IUserInfoService userInfoService;
    private final IEventService eventService;

    @GetMapping(value = "userRegForm")
    public String userRegForm() {
        log.info(this.getClass().getName() + ".user/userRegForm Start!");

        log.info(this.getClass().getName() + ".user/userRegForm End!");

        return "user/userRegForm";
    }

    @ResponseBody
    @PostMapping(value = "getUserIdExists")
    public UserInfoDTO getUserExists(HttpServletRequest request) throws Exception {

        log.info(this.getClass().getName() + ".getUserIdExists Start!");

        String userId = CmmUtil.nvl(request.getParameter("userId")); // 회원아이디

        log.info("userId : " + userId);

        UserInfoDTO pDTO = new UserInfoDTO();
        pDTO.setUserId(userId);

        // 회원아이디를 통해 중복된 아이디인지 조회
        UserInfoDTO rDTO = Optional.ofNullable(userInfoService.getUserIdExists(pDTO)).orElseGet(UserInfoDTO::new);

        log.info(this.getClass().getName() + ".getUserIdExists End!");

        return rDTO;
    }

    @ResponseBody
    @PostMapping(value = "insertUserInfo")
    public MsgDTO insertUserInfo(HttpServletRequest request) throws Exception {

        log.info(this.getClass().getName() + ".insertUserInfo start!");

        int res = 0; // 회원가입 결과
        String msg = ""; //회원가입 결과에 대한 메시지를 전달할 변수
        MsgDTO dto = null; // 결과 메시지 구조

        //웹(회원정보 입력화면)에서 받는 정보를 저장할 변수
        UserInfoDTO pDTO = null;

        try {

            String userId = CmmUtil.nvl(request.getParameter("userId")); //아이디
            String userName = CmmUtil.nvl(request.getParameter("userName")); //이름
            String password = CmmUtil.nvl(request.getParameter("password")); //비밀번호
            String email = CmmUtil.nvl(request.getParameter("email")); //이메일
            String cellPhoneNo = CmmUtil.nvl(request.getParameter("cellphoneNo")); //폰번호

            log.info("userId : " + userId);
            log.info("userName : " + userName);
            log.info("password : " + password);
            log.info("email : " + email);

            pDTO = new UserInfoDTO();
            pDTO.setCellphoneNo(cellPhoneNo);
            pDTO.setUserId(userId);
            pDTO.setUserName(userName);

            //비밀번호는 절대로 복호화되지 않도록 해시 알고리즘으로 암호화함
            pDTO.setPassword(EncryptUtil.encHashSHA256(password));

            //민감 정보인 이메일은 AES128-CBC로 암호화함
            pDTO.setEmail(EncryptUtil.encAES128CBC(email));

            // 회원가입 서비스 호출하여 결과 받기
            res = userInfoService.insertUserInfo(pDTO);

            log.info("회원가입 결과(res) : " + res);

            if (res == 1) {
                msg = "회원가입되었습니다.";

                //추후 회원가입 입력화면에서 ajax를 활용해서 아이디 중복, 이메일 중복을 체크하길 바람
            } else if (res == 2) {
                msg = "이미 가입된 아이디입니다.";

            } else {
                msg = "오류로 인해 회원가입이 실패하였습니다.";

            }

        } catch (Exception e) {
            //저장이 실패되면 사용자에게 보여줄 메시지
            msg = "실패하였습니다. : " + e;
            log.info(e.toString());
            e.printStackTrace();

        } finally {
            // 결과 메시지 전달하기
            dto = new MsgDTO();
            dto.setResult(res);
            dto.setMsg(msg);

            log.info(this.getClass().getName() + ".insertUserInfo End!");
        }

        return dto;
    }

    @GetMapping(value = "login")
    public String login() {
        log.info(this.getClass().getName() + ".user/login Start!");

        log.info(this.getClass().getName() + ".user/login End!");

        return "user/login";
    }


    @ResponseBody
    @PostMapping(value = "loginProc")
    public MsgDTO loginProc(HttpServletRequest request, HttpSession session) throws Exception {

        log.info(this.getClass().getName() + ".loginProc Start!");

        int res = 0; //로그인 처리 결과를 저장할 변수 (로그인 성공 : 1, 아이디, 비밀번호 불일치로인한 실패 : 0, 시스템 에러 : 2)
        String msg = ""; //로그인 결과에 대한 메시지를 전달할 변수
        MsgDTO dto = null; // 결과 메시지 구조

        //웹(회원정보 입력화면)에서 받는 정보를 저장할 변수
        UserInfoDTO pDTO = null;

        try {

            String user_id = CmmUtil.nvl(request.getParameter("user_id")); //아이디
            String password = CmmUtil.nvl(request.getParameter("password")); //비밀번호

            log.info("user_id : " + user_id);
            log.info("password : " + password);

            //웹(회원정보 입력화면)에서 받는 정보를 저장할 변수를 메모리에 올리기
            pDTO = new UserInfoDTO();

            pDTO.setUserId(user_id);

            //비밀번호는 절대로 복호화되지 않도록 해시 알고리즘으로 암호화함
            pDTO.setPassword(EncryptUtil.encHashSHA256(password));

            // 로그인을 위해 아이디와 비밀번호가 일치하는지 확인하기 위한 userInfoService 호출하기
            res = userInfoService.getUserLogin(pDTO);

            log.info("res : " + res);

            if (res == 1) { //로그인 성공

                msg = "로그인이 성공했습니다.";
                ChannelDTO cDTO= new ChannelDTO();
                cDTO=eventService.channelFinder(user_id);
                log.info(cDTO.getChannel());
                session.setAttribute("channel",cDTO.getChannel());
                session.setAttribute("SS_USER_ID", user_id);
                log.info((String) session.getAttribute("channel"));
            } else {
                msg = "아이디와 비밀번호가 올바르지 않습니다.";

            }

        } catch (Exception e) {
            //저장이 실패되면 사용자에게 보여줄 메시지
            msg = "시스템 문제로 로그인이 실패했습니다.";
            res = 2;
            log.info(e.toString());
            e.printStackTrace();

        } finally {
            // 결과 메시지 전달하기
            dto = new MsgDTO();
            dto.setResult(res);
            dto.setMsg(msg);

            log.info(this.getClass().getName() + ".loginProc End!");
        }
        log.info("res : "+dto.getResult());
        log.info("msg : "+dto.getMsg());

        return dto;
    }

    @GetMapping(value = "loginSuccess")
    public String loginSuccess() {
        log.info(this.getClass().getName() + ".user/loginSuccess Start!");

        log.info(this.getClass().getName() + ".user/loginSuccess End!");

        return "user/loginSuccess";
    }
    @GetMapping(value = "idFind")
    public String idFind() {
        log.info(this.getClass().getName() + ".user/idFind Start!");

        log.info(this.getClass().getName() + ".user/idFind End!");

        return "/user/idFind";
    }
    @ResponseBody
    @PostMapping(value = "idFind2")
    public UserInfoDTO idFind2(HttpServletRequest request) {
        log.info(this.getClass().getName() + ".user/idFind2 Start!");
        UserInfoDTO pDTO = null;
        UserInfoDTO rDTO = new UserInfoDTO();
        try {
            String email = CmmUtil.nvl(request.getParameter("email")); //이메일
            pDTO = new UserInfoDTO();
            log.info("이메일 : "+email);
            pDTO.setEmail(EncryptUtil.encAES128CBC(email));
            log.info("이메일 : "+pDTO.getEmail());
            rDTO = userInfoService.idFinder(pDTO);
            log.info("user : "+rDTO.getUserId());
        }catch (Exception e){

        }

        log.info(this.getClass().getName() + ".user/idFind2 End!");

        return rDTO;
    }
    @GetMapping(value = "pwFind")
    public String pwFind() {
        log.info(this.getClass().getName() + ".user/pwFind Start!");

        log.info(this.getClass().getName() + ".user/pwFind End!");

        return "user/pwFind";
    }

    @GetMapping(value = "pwChange")
    public String pwChange() {
        log.info(this.getClass().getName() + ".user/pwChange Start!");
        log.info(this.getClass().getName() + ".user/pwChange End!");

        return "user/pwChange";
    }

    @ResponseBody
    @PostMapping(value = "pwChange2")
    public MsgDTO pwChange2(HttpServletRequest request, ModelMap model) throws Exception {

        log.info(this.getClass().getName() + ".pwChange start!");

        int res = 0;
        String msg = "";
        MsgDTO dto = null;

        UserInfoDTO pDTO = null;

        try {


            String password = CmmUtil.nvl(request.getParameter("password")); //비밀번호
            String email = CmmUtil.nvl(request.getParameter("email")); //이메일

            log.info("password : " + password);
            log.info("email : " + email);

            pDTO = new UserInfoDTO();

            //비밀번호는 절대로 복호화되지 않도록 해시 알고리즘으로 암호화함
            pDTO.setPassword(EncryptUtil.encHashSHA256(password));

            //민감 정보인 이메일은 AES128-CBC로 암호화함
            pDTO.setEmail(EncryptUtil.encAES128CBC(email));

            // 회원가입 서비스 호출하여 결과 받기
            res = userInfoService.pwChange(pDTO);

            log.info("비밀번호 변경 결과(res) : " + res);

            if (res == 1) {
                msg = "비밀번호가 변경되었습니다.";

                //추후 회원가입 입력화면에서 ajax를 활용해서 아이디 중복, 이메일 중복을 체크하길 바람
            } else if (res == 2) {
                msg = "이미 가입된 아이디입니다.";

            } else {
                msg = "오류로 인해 비밀번호 변경이 실패하였습니다.";

            }

        } catch (Exception e) {
            //저장이 실패되면 사용자에게 보여줄 메시지
            msg = "실패하였습니다. : " + e;
            log.info(e.toString());
            e.printStackTrace();

        } finally {
            // 결과 메시지 전달하기
            dto = new MsgDTO();
            dto.setResult(res);
            dto.setMsg(msg);

            log.info(this.getClass().getName() + ".pwChange End!");
        }

        return dto;
    }

    @ResponseBody
    @PostMapping("delId")
    public MsgDTO delId(HttpSession session) throws Exception {
        log.info(this.getClass().getName() +"delId Start");
        MsgDTO dto = new MsgDTO();
        log.info("아이디: "+session.getAttribute("SS_USER_ID"));
        UserInfoDTO pDTO = new UserInfoDTO();
        pDTO.setUserId((String) session.getAttribute(("SS_USER_ID")));
        userInfoService.delId(pDTO);
        session.setAttribute("SS_USER_ID", ""); // 세션 값 빈값으로 변경
        session.removeAttribute("SS_USER_ID"); // 세션 값 지우기
        dto.setResult(1);
        dto.setMsg("회원 탈퇴되었습니다.");
        log.info(this.getClass().getName() +"delId End");
        return dto;
    }

    @ResponseBody
    @PostMapping(value = "logout")
    public MsgDTO logout(HttpSession session) {

        log.info(this.getClass().getName() + ".logout Start!");

        session.setAttribute("SS_USER_ID", ""); // 세션 값 빈값으로 변경
        session.removeAttribute("SS_USER_ID"); // 세션 값 지우기
        session.setAttribute("channel","");
        session.removeAttribute("channel");

        MsgDTO dto = new MsgDTO();
        dto.setResult(1);
        dto.setMsg("로그아웃하였습니다.");

        log.info(this.getClass().getName() + ".logout End!");

        return dto;
    }
    private final JavaMailSender mailSender;
    @RequestMapping(value = "mailCheck", method = RequestMethod.GET)
    @ResponseBody
    public String mailCheck(@RequestParam("sm_email") String sm_email) throws Exception{
        int serti = (int)((Math.random()* (99999 - 10000 + 1)) + 10000);
        log.info("Email : "+sm_email);
        String from = "data686@naver.com";//보내는 이 메일주소
        String to = sm_email;
        String title = "회원가입시 필요한 인증번호 입니다.";
        String content = "[인증번호] "+ serti +" 입니다. <br/> 인증번호 확인란에 기입해주십시오.";
        String num = "";
        try {
            MimeMessage mail = mailSender.createMimeMessage();
            MimeMessageHelper mailHelper = new MimeMessageHelper(mail, true, "UTF-8");
            log.info(from);
            log.info(to);
            log.info(title);
            log.info(content);

            mailHelper.setFrom(from);
            mailHelper.setTo(to);
            mailHelper.setSubject(title);
            mailHelper.setText(content, true);

            log.info(String.valueOf(mail));
            mailSender.send(mail);
            num = Integer.toString(serti);

        } catch(Exception e) {
            num = "error";
        }
        return num;
    }
}
