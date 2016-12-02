package xbog.service;

import xbog.model.Question;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/3.
 */
@Service
public class SearchService {
    //构建客户端
    private static final String SOLR_URL="http://localhost:8983/solr/wenda";
    private HttpSolrClient client=new HttpSolrClient.Builder(SOLR_URL).build();
    //此字段用的多，所以用字段提高复用性
    private static final String QUESTION_TITLE_FIELD="question_title";
    private static final String QUESTION_CONTENT_FIELD="question_content";


    //添加搜索方法
    public Map<String,Object> searchQuestion(String keyword, int offset,int count,
                                         String hlPre,String hlPos) throws Exception{
        Map<String,Object> map=new HashMap<>();

        //获取搜索列表
        List<Question> questionList=new ArrayList<>();
        //新建查询，及设置
        SolrQuery query=new SolrQuery(keyword);
        query.setRows(count);
        query.setStart(offset);
        query.setHighlight(true);
        query.setHighlightSimplePre(hlPre);
        query.setHighlightSimplePost(hlPos);
        query.set("hl.fl",QUESTION_TITLE_FIELD+","+QUESTION_CONTENT_FIELD);
        //搜索response
        QueryResponse response=client.query(query);

        //开始遍历highlighting
        for (Map.Entry<String,  Map< String,List<String> > > entry:response.getHighlighting().entrySet()){
            Question q=new Question();//把读取到的文本设置成一个类
            q.setId(Integer.parseInt(entry.getKey()));
            //把内容和标题取出来
            if (entry.getValue().containsKey(QUESTION_CONTENT_FIELD)){
                List<String> contentList=entry.getValue().get(QUESTION_CONTENT_FIELD);
                if (contentList.size() > 0){
                    q.setContent(contentList.get(0));//list里面其实就只有一条数据
                }
            }

            if (entry.getValue().containsKey(QUESTION_TITLE_FIELD)){
                List<String> titleList=entry.getValue().get(QUESTION_TITLE_FIELD);
                if (titleList.size() > 0){
                    q.setTitle(titleList.get(0));//list里面其实就只有一条数据
                }
            }

            questionList.add(q);
        }

        //2获取搜索总数
        int totalCount = (int)response.getResults().getNumFound();
        map.put("questionList",questionList);
        map.put("totalCount",totalCount);

        return map;
    }

    //添加索引方法
    public boolean indexQuestion(int qid,String title,String content) throws Exception{
        SolrInputDocument doc=new SolrInputDocument();
        doc.setField("id",qid);
        doc.setField(QUESTION_TITLE_FIELD,title);
        doc.setField(QUESTION_CONTENT_FIELD,content);
        UpdateResponse response= client.add(doc,1000);
        return response!=null &&response.getStatus() ==0;
    }





}
