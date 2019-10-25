package com.common.funciton;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 分页工具
 * @Author: wangqiang
 * @Date:2019/6/8 17:11
 */
public class PageUtil {
    public static void main(String[] args) {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 1; i <= 132; i++) {
            list.add("" + i);
        }
        /*PagingResult<String> pagingResult = getCurrentPageList(list, 20, 7, String.class);
        List<String> t = pagingResult.getT();
        for (String s : t) {
            System.out.println(s);
        }*/

        PagesResult<String> pagesResult = listPaging(list, 20, String.class);
        List<CurrentPageInfo<String>> pageData = pagesResult.getCurrentPageData();

        for (CurrentPageInfo<String> currentPageInfo : pageData) {
            System.out.println(currentPageInfo);
        }

    }

    /**
     * 获取当前页list数据
     * @param objects
     * @param pageSize
     * @param currentPageNum
     * @param t
     * @param <T>
     * @return
     */
    public static<T> CurrentPageInfo<T> getCurrentPageList(List<T> objects, int pageSize, int currentPageNum, Class<T> t){
        int totalSize = objects.size();
        int n = (totalSize % pageSize == 0) ? (totalSize / pageSize) : (totalSize / pageSize) + 1;
        CurrentPageInfo currentPageInfo = new CurrentPageInfo();
        currentPageInfo.setTotalSize(totalSize);
        currentPageInfo.setPageSize(pageSize);
        currentPageInfo.setCurrentPageNum(currentPageNum);
        currentPageInfo.setPageNum(n);
        List<T> subList = null;
        if (currentPageNum <= n){
            if (currentPageNum * pageSize > totalSize)
                subList = objects.subList((currentPageNum - 1) * pageSize, totalSize);
            else
                subList = objects.subList((currentPageNum - 1) * pageSize, currentPageNum * pageSize);
            currentPageInfo.setT(subList);
        }
        return currentPageInfo;
    }

    /**
     * 获取所有分页信息 list
     * @param objects
     * @param pageSize
     * @param t
     * @param <T>
     * @return
     */
    public static<T> PagesResult<T> listPaging(List<T> objects, int pageSize, Class<T> t){
        int totalSize = objects.size();
        int n = (totalSize % pageSize == 0) ? (totalSize / pageSize) : (totalSize / pageSize) + 1;

        PagesResult pagesResult = new PagesResult();
        pagesResult.setTotalSize(totalSize);
        pagesResult.setPageSize(pageSize);
        pagesResult.setPageNum(n);
        ArrayList<CurrentPageInfo> currentPageInfos = new ArrayList<>();
        for (int j = 0; j < n; j++) {
            List<T> subList = null;
            CurrentPageInfo currentPageInfo = new CurrentPageInfo();
            if ((j + 1) * pageSize > totalSize)
                subList = objects.subList(j * pageSize, totalSize);
            else
                subList = objects.subList(j * pageSize, (j + 1) * pageSize);
            currentPageInfo.setT(subList);
            currentPageInfo.setCurrentPageNum(j + 1);
            currentPageInfos.add(currentPageInfo);
        }
        pagesResult.setCurrentPageData(currentPageInfos);
        return pagesResult;
    }




    public static class PagesResult<T>{
        private int totalSize;

        private int pageSize;

        private int pageNum;

        private List<CurrentPageInfo<T>> currentPageData;

        public int getTotalSize() {
            return totalSize;
        }

        public void setTotalSize(int totalSize) {
            this.totalSize = totalSize;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public int getPageNum() {
            return pageNum;
        }

        public void setPageNum(int pageNum) {
            this.pageNum = pageNum;
        }

        public List<CurrentPageInfo<T>> getCurrentPageData() {
            return currentPageData;
        }

        public void setCurrentPageData(List<CurrentPageInfo<T>> currentPageData) {
            this.currentPageData = currentPageData;
        }
    }

    public static class CurrentPageInfo<T>{
        private int totalSize;

        private int pageSize;

        private int currentPageNum;

        private int pageNum;

        private List<T> t;

        @Override
        public String toString() {
            return "PagingResult{" +
                    "totalSize=" + totalSize +
                    ", pageSize=" + pageSize +
                    ", currentPageNum=" + currentPageNum +
                    ", pageNum=" + pageNum +
                    ", t=" + t +
                    '}';
        }

        public CurrentPageInfo() {
        }

        public int getTotalSize() {
            return totalSize;
        }

        public void setTotalSize(int totalSize) {
            this.totalSize = totalSize;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public int getCurrentPageNum() {
            return currentPageNum;
        }

        public void setCurrentPageNum(int currentPageNum) {
            this.currentPageNum = currentPageNum;
        }

        public int getPageNum() {
            return pageNum;
        }

        public void setPageNum(int pageNum) {
            this.pageNum = pageNum;
        }

        public List<T> getT() {
            return t;
        }

        public void setT(List<T> t) {
            this.t = t;
        }
    }
}


