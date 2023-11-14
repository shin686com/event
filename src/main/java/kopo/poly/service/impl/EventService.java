package kopo.poly.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import kopo.poly.dto.*;
import kopo.poly.repository.*;
import kopo.poly.repository.entity.*;
import kopo.poly.service.IEventService;
import kopo.poly.util.CmmUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class EventService implements IEventService {
    private final ChannelRepository channelRepository;
    private final EventRepository eventRepository;
    private  final LikeRepository likeRepository;
    private final  TagRepository tagRepository;
    private final  PostTagRepository postTagRepository;

    private final JoinRepository joinRepository;
    private final CommentRepository commentRepository;

    @Autowired
    private SequenceGeneratorService service;
    @Override
    public int insertChannel(ChannelDTO pDTO) throws Exception {

        log.info("insert Channel Start");

        int res = 0;

        String userId = CmmUtil.nvl(pDTO.getUserId());
        String channel = CmmUtil.nvl(pDTO.getChannel());
        String file = CmmUtil.nvl(pDTO.getFile());

        log.info("userId : " + userId);
        log.info("channel : " + channel);
        log.info("file : " + file);


        Optional<ChannelEntitiy> rEntity = channelRepository.findByUserId(userId);


        if (rEntity.isPresent()) {
            res = 2;

        } else {


            ChannelEntitiy pEntity = ChannelEntitiy.builder()
                    .userId(userId).channel(channel).file(file)
                    .build();


            channelRepository.save(pEntity);

            rEntity = channelRepository.findByUserId(userId);

            if (rEntity.isPresent()) {
                res = 1;

            } else {
                res = 0;

            }

        }
        log.info("insert Channel End");
        return res;
    }
    @Override
    public int updateChannel(ChannelDTO pDTO) throws Exception {

        log.info("update Channel Start");

        int res = 0;

        String userId = CmmUtil.nvl(pDTO.getUserId());
        String channel = CmmUtil.nvl(pDTO.getChannel());
        String file = CmmUtil.nvl(pDTO.getFile());

        log.info("userId : " + userId);
        log.info("channel : " + channel);
        log.info("file : " + file);


        Optional<ChannelEntitiy> rEntity = channelRepository.findByUserId(userId);
        ChannelEntitiy lEntity =rEntity.get();
        List<EventEntity> eEntity = eventRepository.findByChannel(lEntity.getChannel());

        for(EventEntity s : eEntity){
            EventEntity mEntity = EventEntity.builder()
                    .eventSeq(s.getEventSeq()).channel(channel).title(s.getTitle()).detail(s.getDetail()).price(s.getPrice())
                    .addr(s.getAddr()).firstimage(s.getFirstimage()).category(s.getCategory()).build();
            eventRepository.save(mEntity);
        }

            ChannelEntitiy pEntity = ChannelEntitiy.builder()
                    .channelSeq(lEntity.getChannelSeq()).userId(lEntity.getUserId()).channel(channel).file(file)
                    .build();


            channelRepository.save(pEntity);

            rEntity = channelRepository.findByUserId(userId);


            if (rEntity.isPresent()) {
                res = 1;

            } else {
                res = 0;

            }

        log.info("update Channel End");
        return res;
    }
    public List<EventEntity> searchEvent(SearchDTO sDTO){
        String title = sDTO.getTitle();
        String addr = sDTO.getCity();
        String category = sDTO.getCategory();
        List<EventEntity> rList=null;
        log.info(title);
        log.info(addr);
        log.info(category);
        if (!category.equals("선택안함")){
            if(!addr.equals("선택안함")){
                if(!title.equals("")){
                    rList = eventRepository.findByTitleContainingAndAddrContainingAndCategoryContaining(title, addr, category);//123
                }else{
                    rList=eventRepository.findByAddrContainingAndCategoryContaining(addr,category);//23
                }
            }else if (!title.equals("")){
                rList=eventRepository.findByTitleContainingAndCategoryContaining(title,category);//13
            }else {
                rList=eventRepository.findByCategoryContaining(category);//3
            }
        }else if(!addr.equals("선택안함")){
            if(!title.equals("")){
                rList=eventRepository.findByTitleContainingAndAddrContaining(title,addr);//12
            }else {
                rList=eventRepository.findByAddrContaining(addr);//2
            }
        }else{
            rList=eventRepository.findByTitleContaining(title);//1
        }
        return rList;
    }

    @Override
    public int insertEvent(EventDTO pDTO) throws Exception {

        log.info("insert Channel Start");
        int res = 0;

        String channel = CmmUtil.nvl(pDTO.getChannel());
        String file = CmmUtil.nvl(pDTO.getFirstimage());
        String title = CmmUtil.nvl(pDTO.getTitle());
        String detail = CmmUtil.nvl(pDTO.getDetail());
        int price = pDTO.getPrice();
        String addr = CmmUtil.nvl(pDTO.getAddr());
        String category = CmmUtil.nvl(pDTO.getCategory());
        String tag = CmmUtil.nvl(pDTO.getTag());

        log.info("channel : " + channel);
        log.info("file : " + file);
        log.info("title : " + title);
        log.info("addr : " + addr);
        log.info("detail : " + detail);
        log.info("price : " + price);
        log.info("category : " + category);
        log.info("tag : " + tag);
        String data[] = tag.split("#");

        for (int i=1; i<data.length; i++){
            log.info("tag["+i+"]"+data[i]);
        }

        Optional<EventEntity> rEntity = eventRepository.findByTitle(title);

        if (rEntity.isPresent()) {
            res = 2;

        } else {

            EventEntity pEntity = EventEntity.builder()
                    .eventSeq(service.getSequenceNumber(EventEntity.SEQUENCE_NAME)).title(title).channel(channel).firstimage(file).addr(addr).detail(detail).price(price).category(category)
                    .readCnt(0L)
                    .build();


            eventRepository.save(pEntity);


            rEntity = eventRepository.findByTitle(title);

            if (rEntity.isPresent()) {
                res = 1;
                for (int i=1; i<data.length; i++) {
                    Optional<TagEntity> tEntity = tagRepository.findByName(data[i]);
                    if(tEntity.isPresent()){
                        TagEntity bEntity = tagRepository.findByName(data[i]).get();
                        EventEntity yEntity = eventRepository.findByTitle(title).get();
                        PostTagEntity aEntity= PostTagEntity.builder()
                                .postid(String.valueOf(yEntity.getEventSeq())).tagid(bEntity.getId()).build();
                        postTagRepository.save(aEntity);
                    }else{
                        TagEntity gEntity = TagEntity.builder()
                                .name(data[i]).build();
                        tagRepository.save(gEntity);
                        TagEntity bEntity = tagRepository.findByName(data[i]).get();
                        EventEntity yEntity = eventRepository.findByTitle(title).get();

                        PostTagEntity aEntity= PostTagEntity.builder()
                                .postid(String.valueOf(yEntity.getEventSeq())).tagid(bEntity.getId()).build();
                        postTagRepository.save(aEntity);

                    }

                }


            } else {
                res = 0;

            }

        }
        log.info("insert Channel End");
        return res;
    }
    public ChannelDTO channelFinder(String userid) throws Exception{
        log.info("채널 조회 서비스 시작");
        log.info("userid : "+userid);
        ChannelDTO rDTO = new ChannelDTO();
        Optional<ChannelEntitiy> lEntity = channelRepository.findByUserId(userid);


        if (lEntity.isPresent()) {
            ChannelEntitiy rEntity = channelRepository.findByUserId(userid).get();
            rDTO = new ObjectMapper().convertValue(rEntity, new TypeReference<ChannelDTO>() {
            });
        }


        log.info("채널명 : "+rDTO.getChannel());
        log.info("파일명 : "+rDTO.getFile());
        log.info("아이디 : "+rDTO.getUserId());
        log.info("채널 조회 서비스 종료");
        return rDTO;
    }
    public List<EventEntity> findEvent2(String channel) throws Exception{
        log.info("채널 조회 서비스 시작");
        log.info("channel : "+channel);
        List<EventEntity> rList = eventRepository.findByChannel(channel);

        log.info("채널 조회 서비스 종료");
        return rList;
    }
    public List<JoinEntity> findEvent3(String userid) throws Exception{
        log.info("채널 조회 서비스 시작");
        List<JoinEntity> rList = joinRepository.findByUserid(userid);

        log.info("채널 조회 서비스 종료");
        return rList;
    }
    public List<EventEntity> findEvent4(String category,Pageable pageable) throws Exception{
        log.info("채널 조회 서비스 시작");
        List<EventEntity> rList = eventRepository.findByCategory(category,pageable);

        log.info("채널 조회 서비스 종료");
        return rList;
    }
    public List<JoinEntity> findTitle(String title) {
        log.info("채널 조회 서비스 시작2");
        List<JoinEntity> rList = joinRepository.findByTitle(title);


        log.info("채널 조회 서비스 종료2");
        return rList;
    }
    public List<EventEntity> delete(String title){
        List<EventEntity> rList=eventRepository.deleteByTitle(title);
        commentRepository.deleteAllByPosts(title);

        return rList;
    }
    public EventDTO findEvent(String user,String title,int i) throws Exception{
        log.info("이벤트 상세 조회 서비스 시작");
        log.info("title : "+title);
        EventDTO rDTO = new EventDTO();
        EventEntity rEntity = eventRepository.findByTitle(title).get();
        EventEntity lEntity = null;

        if(i==0) {
            lEntity = EventEntity.builder()
                    .eventSeq(rEntity.getEventSeq()).title(rEntity.getTitle())
                    .channel(rEntity.getChannel()).firstimage(rEntity.getFirstimage())
                    .detail(rEntity.getDetail())
                    .readCnt(rEntity.getReadCnt() + 1)
                    .price(rEntity.getPrice()).addr(rEntity.getAddr())
                    .category(rEntity.getCategory())
                    .build();
        }else if(i==1){
            lEntity = EventEntity.builder()
                    .eventSeq(rEntity.getEventSeq()).title(rEntity.getTitle())
                    .channel(rEntity.getChannel()).firstimage(rEntity.getFirstimage())
                    .detail(rEntity.getDetail())
                    .readCnt(rEntity.getReadCnt())
                    .price(rEntity.getPrice()).addr(rEntity.getAddr())
                    .category(rEntity.getCategory())
                    .build();
        }
        eventRepository.save(lEntity);
        rDTO = new ObjectMapper().convertValue(lEntity, new TypeReference<EventDTO>() {
        });
        log.info("채널명 : "+rDTO.getChannel());
        log.info("파일명 : "+rDTO.getFirstimage());
        log.info("아이디 : "+rDTO.getTitle());
        log.info("조회수 : "+rDTO.getReadCnt());
        log.info("이벤트 조회 서비스 종료");
        return rDTO;
    }
    public void like(String user,String title,int i){
        Optional good = likeRepository.findByUserAndTitle(user,title);
        LikeEntity lEntity = null;
        if(!good.isPresent()){
            lEntity = LikeEntity.builder()
                    .user(user).title(title).likes(i).build();
            likeRepository.save(lEntity);
        }else{
            LikeEntity gEntity = (LikeEntity) good.get();
            if(gEntity.getLikes()==i){
                likeRepository.deleteById(gEntity.getId());
            }else {
                lEntity = LikeEntity.builder().id(gEntity.getId())
                        .user(user).title(title).likes(i).build();
                likeRepository.save(lEntity);
            }
        }

    }
    public LikeDTO countLike(String title){
        LikeDTO lDTO = new LikeDTO();
        int like = likeRepository.countByTitleAndLikes(title,1);
        int hate = likeRepository.countByTitleAndLikes(title,-1);
        log.info("like : "+like);
        log.info("hate : "+hate);
        lDTO.setGood(like);
        lDTO.setHate(hate);
        return lDTO;
    }
    public int countAll(){
        return eventRepository.findAll().size();
    }
    public int countAll2(String category){
        return eventRepository.findAllByCategory(category).size();
    }
    public int countAll3(String category){
        return eventRepository.findAllByCategory(category).size();
    }

    public List<EventEntity> findAllEvent(Pageable pageable) throws Exception{
        log.info("이벤트 전부 조회 서비스 시작");
        List<EventEntity> sList = new ArrayList<>();
        Page<EventEntity> rList = eventRepository.findAll(pageable);
        sList=rList.getContent();
        log.info("이벤트 전부 서비스 종료");
        return sList;
    }
    public List<TagEntity> tag(String title){
        log.info("태그 조회 시작");
        EventEntity yEntity = eventRepository.findByTitle(title).get();
        List<PostTagEntity> nList = postTagRepository.findByPostid(String.valueOf(yEntity.getEventSeq()));
        log.info("태그 중간");
        List<TagEntity> sList = new ArrayList<>();
        for (int i=0; i<nList.size(); i++){
            sList.add(tagRepository.findById(nList.get(i).getTagid()).get());
        }
        log.info("태그 조회 끝");
        return sList;
    }
    public  List<EventEntity> searchTag(String tag){
        String tagId = tagRepository.findByName(tag).get().getId();
        log.info("태그 아이디 : "+tagId);
        List<PostTagEntity> nList = postTagRepository.findAllByTagid(tagId);
        List<EventEntity> sList = new ArrayList<>();
        for (int i=0; i<nList.size(); i++){
            int n = Integer.parseInt(nList.get(i).getPostid());
            log.info("이벤트 아이디 : "+n);
            sList.add(eventRepository.findByEventSeq(n).get());
        }
        return sList;
    }
    public int join(JoinDTO jDTO)throws Exception{

        int res = 0;

        String userid = CmmUtil.nvl(jDTO.getUserid()); // 아이디
        String name = CmmUtil.nvl(jDTO.getName()); // 이름
        String title = CmmUtil.nvl(jDTO.getTitle()); // 타이틀
        String cellphoneNo = CmmUtil.nvl(jDTO.getCellphoneNo()); //폰번호

        log.info("userId : " + userid);
        log.info("userName : " + name);
        log.info("title : " + title);
        log.info("cellphoneNo : " + cellphoneNo);


        Optional<JoinEntity> rEntity = joinRepository.findByUseridAndTitle(userid,title);


        if (rEntity.isPresent()) {
            res = 2;

        } else {


            JoinEntity pEntity = JoinEntity.builder()
                    .userid(userid).name(name).title(title).cellphoneNo(cellphoneNo)
                    .build();

            joinRepository.save(pEntity);


            rEntity = joinRepository.findByUseridAndTitle(userid,title);

            if (rEntity.isPresent()) {
                res = 1;

            } else {
                res = 0;

            }

        }
        return res;
    }

}
