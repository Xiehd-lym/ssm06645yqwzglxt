package com.spring.controller;

import com.spring.dao.ShenqingwuziMapper;
import com.spring.entity.Shenqingwuzi;
import com.spring.service.ShenqingwuziService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import tk.mybatis.mapper.entity.Example;
import util.Request;
import util.Info;
import dao.Query;
import java.util.*;



import com.spring.entity.Wuzixinxi;
import com.spring.service.WuzixinxiService;

/**
 * 申请物资 */
@Controller
public class ShenqingwuziController extends BaseController
{
    @Autowired
    private ShenqingwuziMapper dao;
    @Autowired
    private ShenqingwuziService service;

    @Autowired
    private WuzixinxiService serviceRead;

    /**
     *  后台列表页
     *
     */
    @RequestMapping("/shenqingwuzi_list")
    public String list()
    {
        // 检测是否有登录，没登录则跳转到登录页面
        if(!checkLogin()){
            return showError("尚未登录" , "./login.do");
        }

        String order = Request.get("order" , "id"); // 获取前台提交的URL参数 order  如果没有则设置为id
        String sort  = Request.get("sort" , "desc"); // 获取前台提交的URL参数 sort  如果没有则设置为desc

        Example example = new Example(Shenqingwuzi.class); //  创建一个扩展搜索类
        Example.Criteria criteria = example.createCriteria();          // 创建一个扩展搜索条件类
        String where = " 1=1 ";   // 创建初始条件为：1=1
        where += getWhere();      // 从方法中获取url 上的参数，并写成 sql条件语句
        criteria.andCondition(where);   // 将条件写进上面的扩展条件类中
        if(sort.equals("desc")){        // 判断前台提交的sort 参数是否等于  desc倒序  是则使用倒序，否则使用正序
            example.orderBy(order).desc();  // 把sql 语句设置成倒序
        }else{
            example.orderBy(order).asc();   // 把 sql 设置成正序
        }
        int page = request.getParameter("page") == null ? 1 : Integer.valueOf(request.getParameter("page"));  // 获取前台提交的URL参数 page  如果没有则设置为1
        page = Math.max(1 , page);  // 取两个数的最大值，防止page 小于1
        List<Shenqingwuzi> list = service.selectPageExample(example , page , 12);   // 获取当前页的行数
        // 将列表写给界面使用
        assign("list" , list);
        assign("orderby" , order);  // 把当前排序结果写进前台
        assign("sort" , sort);      // 把当前排序结果写进前台
        assign("where" , where);    // 把当前条件写给前台
        return "shenqingwuzi_list";   // 使用视图文件：WebRoot\shenqingwuzi_list.jsp
    }



    /**
     *  获取前台搜索框填写的内容,并组成where 语句
     */
    public String getWhere()
    {
        String where = " ";
        // 判断URL 参数wuzixinxiid是否大于0
        if( Request.getInt("wuzixinxiid") > 0 ) {
            // 大于0 则写入条件
            where += " AND wuzixinxiid='"+Request.getInt("wuzixinxiid")+"' ";
        }
        // 以下是判断搜索框中是否有输入内容，判断是否前台是否有填写相关条件，符合则写入sql搜索语句
            if(!Request.get("bianhao").equals("")) {
            where += " AND bianhao LIKE '%"+Request.get("bianhao")+"%' ";
        }
                if(!Request.get("mingcheng").equals("")) {
            where += " AND mingcheng LIKE '%"+Request.get("mingcheng")+"%' ";
        }
                if(!Request.get("leixing").equals("")) {
            where += " AND leixing ='"+Request.get("leixing")+"' ";
        }
                if(!Request.get("shenqingleixing").equals("")) {
            where += " AND shenqingleixing ='"+Request.get("shenqingleixing")+"' ";
        }
                if(!Request.get("xingming").equals("")) {
            where += " AND xingming LIKE '%"+Request.get("xingming")+"%' ";
        }
            return where;
    }

    /**
     * 申请人列表
     */
    @RequestMapping("/shenqingwuzi_list_shenqingren")
    public String listshenqingren()
    {
        // 检测是否有登录，没登录则跳转到登录页面
        if(!checkLogin()){
            return showError("尚未登录" , "./login.do");
        }

        String order = Request.get("order" , "id"); // 获取前台提交的URL参数 order  如果没有则设置为id
        String sort  = Request.get("sort" , "desc"); // 获取前台提交的URL参数 sort  如果没有则设置为desc

        
        Example example = new Example(Shenqingwuzi.class);  //  创建一个扩展搜索类
        Example.Criteria criteria = example.createCriteria();           // 创建一个扩展搜索条件类
        // 初始化一个条件，条件为：申请人=当前登录用户
        String where = " shenqingren='"+session.getAttribute("username")+"' ";
        where += getWhere();

        criteria.andCondition(where);   // 将条件写入
        if(sort.equals("desc")){        // 注释同list
            example.orderBy(order).desc(); // 注释同list
        }else{
            example.orderBy(order).asc(); // 注释同list
        }

        int page = request.getParameter("page") == null ? 1 : Integer.valueOf(request.getParameter("page")); // 注释同list
        page = Math.max(1 , page); // 注释同list

            List<Shenqingwuzi> list = service.selectPageExample(example , page , 12);
        
        request.setAttribute("list" , list);
                assign("orderby" , order);
        assign("sort" , sort);
        assign("where" , where);
        return "shenqingwuzi_list_shenqingren";
    }




        @RequestMapping("/shenqingwuzi_add")
    public String add()
    {
        int id = Request.getInt("id");  // 根据id 获取 物资信息模块中的数据
        Wuzixinxi readMap = serviceRead.find(id);
        // 将数据行写入给前台jsp页面
        request.setAttribute("readMap" , readMap);
        return "shenqingwuzi_add";
    }

    @RequestMapping("/shenqingwuziadd")
    public String addWeb()
    {
        if(!checkLogin()){
            return showError("尚未登录" , "./");
        }
        int id = Request.getInt("id"); // 根据id 获取 物资信息模块中的数据
        Wuzixinxi readMap = serviceRead.find(id);
        request.setAttribute("readMap" , readMap);
        return "shenqingwuziadd";
    }


    @RequestMapping("/shenqingwuzi_updt")
    public String updt()
    {
        int id = Request.getInt("id");
        // 获取行数据，并赋值给前台jsp页面
        Shenqingwuzi mmm = service.find(id);
        request.setAttribute("mmm" , mmm);
        request.setAttribute("updtself" , 0);
        return "shenqingwuzi_updt";
    }
    /**
     * 添加内容
     * @return
     */
    @RequestMapping("/shenqingwuziinsert")
    public String insert()
    {
        String tmp="";
        Shenqingwuzi post = new Shenqingwuzi();  // 创建实体类
        // 设置前台提交上来的数据到实体类中
        post.setBianhao(Request.get("bianhao"));

        post.setMingcheng(Request.get("mingcheng"));

        post.setLeixing(Request.get("leixing"));

        post.setShenqingleixing(Request.get("shenqingleixing"));

        post.setShenqingshuliang(Request.getInt("shenqingshuliang"));

        post.setXingming(Request.get("xingming"));

        post.setLianxidianhua(Request.get("lianxidianhua"));

        post.setDizhi(Request.get("dizhi"));

        post.setZhuangtai(Request.get("zhuangtai"));

        post.setShenqingmiaoshu(Request.get("shenqingmiaoshu"));

        post.setShenqingren(Request.get("shenqingren"));

        post.setWuzixinxiid(Request.getInt("wuzixinxiid"));

        
        post.setAddtime(Info.getDateStr()); // 设置添加时间
                service.insert(post); // 插入数据
        int charuid = post.getId().intValue();
        
        return showSuccess("保存成功" , Request.get("referer").equals("") ? request.getHeader("referer") : Request.get("referer"));
    }

    /**
    * 更新内容
    * @return
    */
    @RequestMapping("/shenqingwuziupdate")
    public String update()
    {
        // 创建实体类
        Shenqingwuzi post = new Shenqingwuzi();
        // 将前台表单数据填充到实体类
        if(!Request.get("bianhao").equals(""))
        post.setBianhao(Request.get("bianhao"));
                if(!Request.get("mingcheng").equals(""))
        post.setMingcheng(Request.get("mingcheng"));
                if(!Request.get("leixing").equals(""))
        post.setLeixing(Request.get("leixing"));
                if(!Request.get("shenqingleixing").equals(""))
        post.setShenqingleixing(Request.get("shenqingleixing"));
                if(!Request.get("shenqingshuliang").equals(""))
        post.setShenqingshuliang(Request.getInt("shenqingshuliang"));
            if(!Request.get("xingming").equals(""))
        post.setXingming(Request.get("xingming"));
                if(!Request.get("lianxidianhua").equals(""))
        post.setLianxidianhua(Request.get("lianxidianhua"));
                if(!Request.get("dizhi").equals(""))
        post.setDizhi(Request.get("dizhi"));
                if(!Request.get("zhuangtai").equals(""))
        post.setZhuangtai(Request.get("zhuangtai"));
                if(!Request.get("shenqingmiaoshu").equals(""))
        post.setShenqingmiaoshu(Request.get("shenqingmiaoshu"));
                if(!Request.get("shenqingren").equals(""))
        post.setShenqingren(Request.get("shenqingren"));
        
        post.setId(Request.getInt("id"));
                service.update(post); // 更新数据
        int charuid = post.getId().intValue();
                        return showSuccess("保存成功" , Request.get("referer")); // 弹出保存成功，并跳转到前台提交的 referer 页面
    }
    /**
     *  后台详情
     */
    @RequestMapping("/shenqingwuzi_detail")
    public String detail()
    {
        int id = Request.getInt("id");
        Shenqingwuzi map = service.find(id);  // 根据前台url 参数中的id获取行数据
        request.setAttribute("map" , map);  // 把数据写到前台
        return "shenqingwuzi_detail";  // 详情页面：WebRoot\shenqingwuzi_detail.jsp
    }
        /**
    *  删除
    */
    @RequestMapping("/shenqingwuzi_delete")
    public String delete()
    {
        if(!checkLogin()){
            return showError("尚未登录");
        }
        int id = Request.getInt("id");  // 根据id 删除某行数据
        HashMap map = Query.make("shenqingwuzi").find(id);

                service.delete(id);// 根据id 删除某行数据
                return showSuccess("删除成功",request.getHeader("referer"));//弹出删除成功，并跳回上一页
    }
}
