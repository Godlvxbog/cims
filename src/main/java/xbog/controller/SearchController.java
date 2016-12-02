package xbog.controller;


import xbog.constant.AttributeConstant;
import xbog.model.EntityType;
import xbog.model.Question;
import xbog.model.ViewOfObject;
import xbog.service.FollowService;
import xbog.service.QuestionService;
import xbog.service.SearchService;
import xbog.service.UserService;
import xbog.util.Pager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Controller
public class SearchController {
    private static final Logger logger = LoggerFactory.getLogger(SearchController.class);

    @Autowired
    SearchService searchService;
    @Autowired
    FollowService followService;
    @Autowired
    UserService userService;
    @Autowired
    QuestionService questionService;

    @RequestMapping(path = "/search")
    public String search(@RequestParam(value = "pageIndex", defaultValue = "1") Integer pageIndex,
                         Model model,
                         @RequestParam("q") String keyword,
                         @RequestParam(value = "offset",defaultValue = "0") int offset,
                         @RequestParam(value = "count",defaultValue = "10") int count){
        try{
            Map<String,Object> map=searchService.searchQuestion(keyword,offset,count,"<strong>","</strong>");
            int totalCount=(int)map.get("totalCount");
            Pager pager=new Pager(pageIndex,10,totalCount);

            List<Question> questionList=(List<Question>)searchService.searchQuestion(keyword,pager.getStartIndex(),count,"<strong>","</strong>").get("questionList");


            System.out.println(questionList.size());
            List<ViewOfObject> vos=new ArrayList<>();
            for (Question question : questionList) {
                Question q=questionService.selectById(question.getId());//数据库中的

                if (question.getContent()!=null){
                    q.setContent(question.getContent());

                }
                if (question.getTitle()!=null){
                    q.setTitle(question.getTitle());
                    System.out.println(question.getTitle());
                }

                ViewOfObject vo = new ViewOfObject();
                vo.set("question", q);
                vo.set("followCount",followService.getFollowerCount(EntityType.ENTITY_QUESTION,question.getId()));
                vo.set("user", userService.getUser(q.getUserId()));
                vos.add(vo);
            }
            model.addAttribute("vos",vos);
            model.addAttribute("keyword",keyword);
            model.addAttribute("totalCount",totalCount);
            model.addAttribute(AttributeConstant.PAGER, pager);

        }catch (Exception e){
            logger.error("搜索结果失败"+e.getMessage());
        }

        return "result";
    }

}
