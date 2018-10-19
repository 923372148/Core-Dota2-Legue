package hopnetworks.dota2;


import com.alibaba.fastjson.JSONObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.util.JSON;
import hopnetworks.dota2.DAO.MatchModelRepository;
import hopnetworks.dota2.DAO.MatchRepository;
import hopnetworks.dota2.DAO.TeamRepository;
import hopnetworks.dota2.Utils.HttpRequestUtil;
import hopnetworks.dota2.domain.Match;
import hopnetworks.dota2.domain.MatchModel;
import hopnetworks.dota2.domain.Team;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.Collection;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Dota2ApplicationTests {
    @Autowired
    private MatchRepository matchRepository;
    @Autowired
    private MatchModelRepository matchModelRepository;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    HttpRequestUtil httpRequestUtil;
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Test
    public void contextLoads() {
Match match=new Match();
match.setMatchId("match1");
match.setPassWord("155");
match.setUserName("hops");
        logger .info("Mongodb虚拟数据插入测试成功") ;
        matchRepository.save(match);

        String url = "https://api.opendota.com/api/matches/2611524";
        //post请求
        HttpMethod method =HttpMethod.POST;
        // 封装参数，千万不要替换为Map与HashMap，否则参数无法传递
        MultiValueMap<String, String> params= new LinkedMultiValueMap<String, String>();
        //  params.add("access_token", "xxxxx");
        //发送http请求并返回结果
        String result= httpRequestUtil.client(url,method,params);
        logger .info("API获取远程OpenDota2比赛结果测试成功") ;
        MatchModel matchModel=new MatchModel();
        matchModel.setMatchJson((DBObject) JSON.parse(result));
        DBObject matchJson=(DBObject) JSON.parse(result);//将返回结果转换成可供DB存储的数据
        DBObject playersJson=(DBObject)matchJson.get("players");
        playersJson.get("0");
        logger .info( "0号选手数据");
        System.out.println( playersJson.get("0"));
        System.out.println(result);
Team team=new Team();
team.setPlayersJson((DBObject) ((DBObject) JSON.parse(result)).get("players"));
//System.out.println( JSON.parse(result).get("players"));
teamRepository.save(team);

      JSONObject jsonObject=com.alibaba.fastjson.JSON.parseObject(result);
//collection=matchModel.getMatchJson().get("Players");

matchModelRepository.save(matchModel);


    }

}
