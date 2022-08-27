package com.tj.serve.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tj.serve.domain.*;
import com.tj.serve.service.*;
import com.tj.serve.utils.DownLoadWorld;
import com.tj.serve.vo.HotQueryVo;
import com.tj.serve.vo.HotVolVo;
import com.tj.serve.vo.ThesisQueryVo;
import com.tj.serve.vo.ThesisVo;
import com.tj.serve.mapper.ThesisMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.NumberUtils;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author cj
 * @since 2022-02-12
 */
@Service
public class ThesisServiceImpl extends ServiceImpl<ThesisMapper, Thesis> implements ThesisService {

    @Autowired
    private ThesisAddressService thesisAddressService;
    @Autowired
    private TheisContentService theisContentService;
    @Autowired
    private WorldAddressService worldAddressService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private WorldService worldService;
    @Override
    public void addThesis(ThesisVo thesisVo) {
        Thesis thesis = new Thesis();
        BeanUtils.copyProperties(thesisVo,thesis);
        baseMapper.insert(thesis);
    }

    @Override
    public Page<Thesis> getPageThesis(Long current, Long limit, ThesisQueryVo thesisQueryVo) {
        String disciplineType = thesisQueryVo.getDisciplineType();
        String end = thesisQueryVo.getEnd();
        String thesisType = thesisQueryVo.getThesisType();
        String start = thesisQueryVo.getStart();
        QueryWrapper<Thesis> thesisQueryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(disciplineType))
            thesisQueryWrapper.eq("discipline_type",disciplineType);
        if (!StringUtils.isEmpty(thesisType))
            thesisQueryWrapper.eq("thesis_type",thesisType);
        if (!StringUtils.isEmpty(start))
            thesisQueryWrapper.eq("publication_year",start);
        if (!StringUtils.isEmpty(end))
            thesisQueryWrapper.eq("publication_year",end);
        Page<Thesis> thesisPage = new Page<>(current,limit);
        baseMapper.selectPage(thesisPage,thesisQueryWrapper);
        return thesisPage;
    }



    @Override
    public List<HotVolVo> getHotVol(HotQueryVo hotQueryVo) {
        System.out.println("thesisId:"+hotQueryVo.getThesisId());
        Integer thesisId = hotQueryVo.getThesisId();
        Integer search = hotQueryVo.getSearch();
        if (thesisId==null)
        {
            Thesis thesis = baseMapper.selectOne(new QueryWrapper<Thesis>().orderByDesc("id").last("limit 1"));
            thesisId=thesis.getId();
            System.out.println("thesisId:"+thesisId.toString());
        }
        if (search==null)
            search = 1;
        //1.先根据论文的id查找到论文的内容
        List<TheisContent> contentList = theisContentService.getPdfContent(thesisId);
        //根据地址进行读取论文的内容
        Integer pid = null;
        //统计字符并且排序
        StringBuilder sb =new StringBuilder();
        for (TheisContent thesisContent : contentList) {
            //去除空白符
            pid = thesisContent.getPid();
            sb.append(StringUtils.trimWhitespace(thesisContent.getContent()));
        }
        //去除逗号
        String pdfContent = StringUtils.trimWhitespace(sb.toString()).toString();
        pdfContent = pdfContent.replaceAll("\\["," ")
                .replaceAll("]"," ").replaceAll("\\("," ")
                .replaceAll("\\)"," ").replaceAll(","," ")
                .replaceAll("\\'"," ").replaceAll("\\-"," ")
                .replaceAll("[0-9]+"," ").
                        replaceAll("\\."," ").replaceAll("\\%"," ")
        .replaceAll("\\:"," ").replaceAll("https"," ").
                        replaceAll("www"," ").replaceAll("\\“"," ")
        .replaceAll("\\/","").replaceAll("\\;"," ")
        .replaceAll("\\”"," ").replaceAll("[\"]"," ")
        .replaceAll("\\@"," ").replaceAll("\\{"," ").
        replaceAll("\\}"," ").replaceAll("–"," ").replaceAll("\\’"," ")
        .replaceAll("IEEE"," ");
        //根据得到的内容进行分析
        String[] split = pdfContent.split(" ");
        List<String> worldList = new ArrayList<>();
        Map<String, Integer> sortMap = new HashMap<>();
        for (String s : split) {
                s=StringUtils.trimAllWhitespace(s);
            if (!s.isEmpty()&&s.length()>3)
                worldList.add(s);
        }
        if (search.equals(1)){
            sortMap = oneHot(worldList,pid);
        }else  if (search.equals(2)){
            sortMap = twoHot(worldList,pid);
        }else if (search.equals(3)){
            sortMap = threeHot(worldList,pid);
        }else if (search.equals(4)){
            sortMap = fourHot(worldList,pid);
        }else if (search.equals(5)){
            sortMap = fiveHot(worldList,pid);
        }
        //将排序好的map返回
        Set<Map.Entry<String, Integer>> entries = sortMap.entrySet();
        List<HotVolVo> hotVolVoList = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : entries) {
            HotVolVo hotVolVo = new HotVolVo();
            hotVolVo.setVol(entry.getKey());
            hotVolVo.setCount(entry.getValue());
            hotVolVoList.add(hotVolVo);
            hotVolVo = null;
        }
        return hotVolVoList;
    }

    @Override
    public List<HotVolVo> getCateHotVol(HotQueryVo hotQueryVo) {
        //首先先获取论文的分类和要查找的几个词组
        Integer search = hotQueryVo.getSearch();
        Integer th_id = hotQueryVo.getTH_id();
        if (th_id==null){
            return null;
        }
        if (search==null||search<=0){
            search = 1;
        }
        //查找所有的分类
        QueryWrapper<Thesis> wrapper =new QueryWrapper<>();
        wrapper.eq("discipline_type",th_id);
        wrapper.select("id");
        List<Thesis> list = baseMapper.selectList(wrapper);
        StringBuilder stringBuilder = new StringBuilder();
        //循环遍历列表将每个对应id的论文内容取出
        for (Thesis thesis : list) {
            //获取id
            String content= theisContentService.getContent(thesis.getId());
            stringBuilder.append(content);
        }
        List<HotVolVo> hotVolVoList = new ArrayList<>();
        //将该类所有论文加载一起的数字存入redis中，再次查询如果相等的时候，则不需要再次进行查询了
        Integer contentLength = (Integer)redisTemplate.opsForValue().get(th_id);
        if (!Objects.equals(contentLength,stringBuilder.length())){
            redisTemplate.opsForValue().set(th_id,stringBuilder.length());
        }
        String cishu =(String) redisTemplate.opsForValue().get(th_id.toString() + "_" + search);
        if (cishu==null){
            redisTemplate.opsForValue().set(th_id.toString() + "_" + search,search.toString());
        }else if (!cishu.contains(search.toString())){
            redisTemplate.opsForValue().set(th_id.toString() + "_" + search,cishu+","+search.toString());
        }else {
            //如果相等话，说明查询的一样，可以使用查询数据库的数据
            //根据th_id和次数进行查询
            QueryWrapper<World> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("cishu",search);
            queryWrapper.eq("th_id",th_id);
            World world = worldService.getOne(queryWrapper);
            if (world==null){
               return  chuli(stringBuilder,search,th_id);
            }
            //将单词取出1
            String worldWorld = world.getWorld();
            //将字符串转化成JSON格式的数据
            hotVolVoList = JSONObject.parseArray(worldWorld, HotVolVo.class);
            return hotVolVoList;
        }
        return  chuli(stringBuilder,search,th_id);
    }
   //处理数据并分析
    public List<HotVolVo> chuli(StringBuilder stringBuilder,Integer search,Integer th_id ){
        List<HotVolVo> hotVolVoList = new ArrayList<>();
        //根据得到的内容进行分析
        String[] split = stringBuilder.toString().split(" ");
        List<String> worldList = new ArrayList<>();
        Map<String, Integer> sortMap = new HashMap<>();
        for (String s : split) {
            s=StringUtils.trimAllWhitespace(s);
            if (!s.isEmpty()&&s.length()>3)
                worldList.add(s);
        }
        if (search.equals(1)){
            sortMap = oneHot(worldList,th_id);
        }else  if (search.equals(2)){
            sortMap = twoHot(worldList,th_id);
        }else if (search.equals(3)){
            sortMap = threeHot(worldList,th_id);
        }else if (search.equals(4)){
            sortMap = fourHot(worldList,th_id);
        }else if (search.equals(5)){
            sortMap = fiveHot(worldList,th_id);
        }
        //将排序好的map返回
        Set<Map.Entry<String, Integer>> entries = sortMap.entrySet();
        for (Map.Entry<String, Integer> entry : entries) {
            HotVolVo hotVolVo = new HotVolVo();
            hotVolVo.setVol(entry.getKey());
            hotVolVo.setCount(entry.getValue());
            hotVolVoList.add(hotVolVo);
        }
        //将数据插入到数据库中
        //将数据转化成字符串
        String jsonString = JSONObject.toJSONString(hotVolVoList);
        worldService.save(new World(null,th_id,search,jsonString));
        return hotVolVoList;
    }
    //循环遍历1个单词的热门词
    public Map<String,Integer> oneHot(List<String> split,Integer pid ){
        Map<String,Integer> map = new HashMap<>();
        for (int i =0;i<split.size();i++){
            if (map.get(split.get(i))!=null){
                map.put(split.get(i),map.get(split.get(i))+1);
            }else {
                map.put(split.get(i),1);
            }
        }

        Map<String, Integer> sortMap = sortMap(map,pid,1);
        return sortMap;
    }
    //循环遍历2个单词的热门词
    public Map<String,Integer> twoHot(List<String> split,Integer pid){
        Map<String,Integer> map = new HashMap<>();
        for (int i =0;i<split.size()-1;i++){
            if (map.get(split.get(i)+" "+split.get(i+1))!=null){
                map.put(split.get(i)+" "+split.get(i+1),map.get(split.get(i)+" "+split.get(i+1))+1);
            }else {
                map.put(split.get(i)+" "+split.get(i+1),1);
            }
        }
        //排序
        Map<String, Integer> sortMap = sortMap(map,pid,2);
        return sortMap;
    }
    //循环遍历3个单词的热门词
    public Map<String,Integer> threeHot(List<String> split,Integer pid){
        Map<String,Integer> map = new HashMap<>();
        for (int i =0;i<split.size()-2;i++){
            String world = split.get(i)+" "+split.get(i+1)+" "+split.get(i+2);
            if (map.get(world)!=null){
                map.put(world,map.get(world)+1);
            }else {
                map.put(world,1);
            }
        }

        Map<String, Integer> sortMap = sortMap(map,pid,3);
        return sortMap;
    }
    private Map<String, Integer> fourHot(List<String> split, Integer pid) {
        Map<String,Integer> map = new HashMap<>();
        for (int i =0;i<split.size()-3;i++){
            String world = split.get(i)+" "+split.get(i+1)+" "+split.get(i+2)+" "+split.get(i+3);
            if (map.get(world)!=null){
                map.put(world,map.get(world)+1);
            }else {
                map.put(world,1);
            }
        }

        Map<String, Integer> sortMap = sortMap(map,pid,4);
        return sortMap;
    }
    private Map<String, Integer> fiveHot(List<String> split, Integer pid) {
        Map<String,Integer> map = new HashMap<>();
        for (int i =0;i<split.size()-4;i++){
            String world = split.get(i)+" "+split.get(i+1)+" "+split.get(i+2)+" "+split.get(i+3)+" "+split.get(i+4);
            if (map.get(world)!=null){
                map.put(world,map.get(world)+1);
            }else {
                map.put(world,1);
            }
        }

        Map<String, Integer> sortMap = sortMap(map,pid,5);
        return sortMap;
    }
    //用于下载所有的热点词
    public  void downloadWorld(List<Map.Entry<String,Integer>> sortMap,Integer pid,Integer cishu){
        //判断连接是否存在
        QueryWrapper<WorldAddress> wrapper = new QueryWrapper<>();
        wrapper.eq("pid",pid);
        wrapper.eq("cishu",cishu);
        WorldAddress worldAddress = worldAddressService.getOne(wrapper);
        if (worldAddress!=null)
            return;
        String url = DownLoadWorld.downloadWorld(sortMap, pid, cishu);
        worldAddressService.insertWorldAddress(pid,cishu,url);
    }
    public Map<String,Integer> sortMap(Map<String,Integer> map,Integer pid,Integer cishu){
        List<Map.Entry<String,Integer>> list = new ArrayList<Map.Entry<String,Integer>>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });
        downloadWorld(list,pid,cishu);
        Map<String,Integer> top10Data = new HashMap<>();
        int count = 0;

        //输出10个
        if (list.size()>10){
            for (Map.Entry<String, Integer> entry : list) {
                if (count<10){
                    top10Data.put(entry.getKey(),entry.getValue());
                    count++;
                }else break;
            }
        }else {
            for (Map.Entry<String, Integer> entry : list) {
                top10Data.put(entry.getKey(),entry.getValue());
            }
        }
        return top10Data;
    }
    @Override
    public List<Thesis> getAllThesis() {
        List<Thesis> thesisList = baseMapper.selectList(null);
        return thesisList;
    }


}
