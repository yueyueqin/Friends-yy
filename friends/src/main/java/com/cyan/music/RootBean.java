package com.cyan.music;

/**
 * Created by kys-34 on 2016/3/31.
 */
import java.util.List;

public class RootBean {
    private String showapi_res_code;
    private String showapi_res_error;
    private ResBody showapi_res_body;

    public String getShowapi_res_code() {
        return showapi_res_code;
    }
    public void setShowapi_res_code(String showapi_res_code) {
        this.showapi_res_code = showapi_res_code;
    }
    public String getShowapi_res_error() {
        return showapi_res_error;
    }
    public void setShowapi_res_error(String showapi_res_error) {
        this.showapi_res_error = showapi_res_error;
    }
    public ResBody getShowapi_res_body() {
        return showapi_res_body;
    }
    public void setShowapi_res_body(ResBody showapi_res_body) {
        this.showapi_res_body = showapi_res_body;
    }

    @Override
    public String toString() {
        return "RootBean [showapi_res_code=" + showapi_res_code
                + ", showapi_res_error=" + showapi_res_error
                + ", showapi_res_body=" + showapi_res_body + "]";
    }

    public class ResBody{
        private Pagebean pagebean;

        public Pagebean getPagebean() {
            return pagebean;
        }

        public void setPagebean(Pagebean pagebean) {
            this.pagebean = pagebean;
        }

        @Override
        public String toString() {
            return "ResBody [pagebean=" + pagebean + "]";
        }

    }
    public class Pagebean{
        private String allNum;
        private String allPages;
        private List<Content> contentlist;
        private String currentPage;
        private String keyword;
        private String maxResult;
        private String ret_code;
        public String getAllNum() {
            return allNum;
        }
        public void setAllNum(String allNum) {
            this.allNum = allNum;
        }
        public String getAllPages() {
            return allPages;
        }
        public void setAllPages(String allPages) {
            this.allPages = allPages;
        }
        public List<Content> getContentlist() {
            return contentlist;
        }
        public void setContentlist(List<Content> contentlist) {
            this.contentlist = contentlist;
        }
        public String getCurrentPage() {
            return currentPage;
        }
        public void setCurrentPage(String currentPage) {
            this.currentPage = currentPage;
        }
        public String getKeyword() {
            return keyword;
        }
        public void setKeyword(String keyword) {
            this.keyword = keyword;
        }
        public String getMaxResult() {
            return maxResult;
        }
        public void setMaxResult(String maxResult) {
            this.maxResult = maxResult;
        }
        public String getRet_code() {
            return ret_code;
        }
        public void setRet_code(String ret_code) {
            this.ret_code = ret_code;
        }
        @Override
        public String toString() {
            return "Pagebean [allNum=" + allNum + ", allPages=" + allPages
                    + ", contentlist=" + contentlist + ", currentPage="
                    + currentPage + ", keyword=" + keyword + ", maxResult="
                    + maxResult + ", ret_code=" + ret_code + "]";
        }

    }
    public class Content{
        private String albumid;
        private String albumname;
        private String albumpic_big;
        private String albumpic_small;
        private String downUrl;
        private String m4a;
        private String singerid;
        private String singername;
        private String songid;
        private String songname;
        public String getAlbumid() {
            return albumid;
        }
        public void setAlbumid(String albumid) {
            this.albumid = albumid;
        }
        public String getAlbuname() {
            return albumname;
        }
        public void setAlbuname(String albuname) {
            this.albumname = albuname;
        }
        public String getAlbumpic_big() {
            return albumpic_big;
        }
        public void setAlbumpic_big(String albumpic_big) {
            this.albumpic_big = albumpic_big;
        }
        public String getAlbumpic_small() {
            return albumpic_small;
        }
        public void setAlbumpic_small(String albumpic_small) {
            this.albumpic_small = albumpic_small;
        }
        public String getDownUrl() {
            return downUrl;
        }
        public void setDownUrl(String downUrl) {
            this.downUrl = downUrl;
        }
        public String getM4a() {
            return m4a;
        }
        public void setM4a(String m4a) {
            this.m4a = m4a;
        }
        public String getSingerid() {
            return singerid;
        }
        public void setSingerid(String singerid) {
            this.singerid = singerid;
        }
        public String getSingername() {
            return singername;
        }
        public void setSingername(String singername) {
            this.singername = singername;
        }
        public String getSongid() {
            return songid;
        }
        public void setSongid(String songid) {
            this.songid = songid;
        }
        public String getSongname() {
            return songname;
        }
        public void setSongname(String songname) {
            this.songname = songname;
        }
        @Override
        public String toString() {
            return "downUrl=" + downUrl + ", m4a=" + m4a
                    + ", singername=" +  singername
                    + ", songname=" + songname +  "";
        }

    }
}
