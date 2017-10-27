package com.goockr.inductioncooker.data;




import com.goockr.inductioncooker.models.MoreAdapterModel;
import com.goockr.inductioncooker.models.MyAbStractSection;

import java.util.ArrayList;
import java.util.List;

/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
public class DataServer {

    private static final String HTTPS_AVATARS1_GITHUBUSERCONTENT_COM_LINK = "https://avatars1.githubusercontent.com/u/7698209?v=3&s=460";
    private static final String CYM_CHAD = "CymChad";

    private DataServer() {
    }



    public static List<MyAbStractSection> getSampleData() {
        List<MyAbStractSection> list = new ArrayList<>();
        list.add(new MyAbStractSection(true, "Section 1", false));
        list.add(new MyAbStractSection(new MoreAdapterModel("添加设备", "",true)));

//        list.add(new MySection(true, "Section 2", true));
//        list.add(new MySection(new MoreAdapterModel("用户名", CYM_CHAD)));
//        list.add(new MySection(new MoreAdapterModel(HTTPS_AVATARS1_GITHUBUSERCONTENT_COM_LINK, CYM_CHAD)));
//        list.add(new MySection(new MoreAdapterModel(HTTPS_AVATARS1_GITHUBUSERCONTENT_COM_LINK, CYM_CHAD)));
//        list.add(new MySection(true, "Section 3", false));
//        list.add(new MySection(new Video(HTTPS_AVATARS1_GITHUBUSERCONTENT_COM_LINK, CYM_CHAD)));
//        list.add(new MySection(new Video(HTTPS_AVATARS1_GITHUBUSERCONTENT_COM_LINK, CYM_CHAD)));
//        list.add(new MySection(true, "Section 4", false));
//        list.add(new MySection(new Video(HTTPS_AVATARS1_GITHUBUSERCONTENT_COM_LINK, CYM_CHAD)));
//        list.add(new MySection(new Video(HTTPS_AVATARS1_GITHUBUSERCONTENT_COM_LINK, CYM_CHAD)));
//        list.add(new MySection(true, "Section 5", false));
//        list.add(new MySection(new Video(HTTPS_AVATARS1_GITHUBUSERCONTENT_COM_LINK, CYM_CHAD)));
//        list.add(new MySection(new Video(HTTPS_AVATARS1_GITHUBUSERCONTENT_COM_LINK, CYM_CHAD)));
        return list;
    }

    public static List<String> getStrData() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            String str = HTTPS_AVATARS1_GITHUBUSERCONTENT_COM_LINK;
            if (i % 2 == 0) {
                str = CYM_CHAD;
            }
            list.add(str);
        }
        return list;
    }




}
