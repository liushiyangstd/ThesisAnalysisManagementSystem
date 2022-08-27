package com.tj.serve;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import com.tj.serve.vo.HotVolVo;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class Test {

    @org.junit.Test
    public void ss() throws IOException {
//        System.out.println("ssss");
//        HotVolVo volVo = new HotVolVo();
//        volVo.setCount(20);
//        volVo.setVol("a");
//        List<HotVolVo> list = new ArrayList<>();
//        list.add(volVo);
        String fileName = "src/main/resources/write1.xlsx";
        File file = new File(fileName);
        file.createNewFile();
        System.out.println(file.exists());
//        ExcelWriterBuilder write = EasyExcel.write(fileName, HotVolVo.class);
//        ExcelWriterSheetBuilder sheet = write.sheet("单词");
       // EasyExcel.write(fileName,HotVolVo.class).sheet("单词").doWrite(list);
//        File file = new File(fileName);
//        InputStream out = new FileInputStream(file);

//        System.out.println(file.);
        //EasyExcel.write(fileName, DemoData.class).sheet("学生列表").doWrite(data());
    }
}
