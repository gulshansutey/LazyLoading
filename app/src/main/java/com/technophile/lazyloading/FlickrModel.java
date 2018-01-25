package com.technophile.lazyloading;

import com.google.gson.annotations.Expose;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Technophile on 1/25/2018.
 */

public class FlickrModel {

    @Expose
    private Photos photos;
    @Expose
    private String stat;

    /**
     * @return The photos
     */
    public Photos getPhotos() {
        return photos;
    }

    /**
     * @param photos The photos
     */
    public void setPhotos(Photos photos) {
        this.photos = photos;
    }

    /**
     * @return The stat
     */
    public String getStat() {
        return stat;
    }

    /**
     * @param stat The stat
     */
    public void setStat(String stat) {
        this.stat = stat;
    }


   public class Photo {

        @Expose
        private String id;
        @Expose
        private String owner;
        @Expose
        private String secret;
        @Expose
        private String server;
        @Expose
        private int farm;
        @Expose
        private String title;
        @Expose
        private int ispublic;
        @Expose
        private int isfriend;
        @Expose
        private int isfamily;

        /**
         * @return The id
         */
        public String getId() {
            return id;
        }

        /**
         * @param id The id
         */
        public void setId(String id) {
            this.id = id;
        }

        /**
         * @return The owner
         */
        public String getOwner() {
            return owner;
        }

        /**
         * @param owner The owner
         */
        public void setOwner(String owner) {
            this.owner = owner;
        }

        /**
         * @return The secret
         */
        public String getSecret() {
            return secret;
        }

        /**
         * @param secret The secret
         */
        public void setSecret(String secret) {
            this.secret = secret;
        }

        /**
         * @return The server
         */
        public String getServer() {
            return server;
        }

        /**
         * @param server The server
         */
        public void setServer(String server) {
            this.server = server;
        }

        /**
         * @return The farm
         */
        public int getFarm() {
            return farm;
        }

        /**
         * @param farm The farm
         */
        public void setFarm(int farm) {
            this.farm = farm;
        }

        /**
         * @return The title
         */
        public String getTitle() {
            return title;
        }

        /**
         * @param title The title
         */
        public void setTitle(String title) {
            this.title = title;
        }

        /**
         * @return The ispublic
         */
        public int getIspublic() {
            return ispublic;
        }

        /**
         * @param ispublic The ispublic
         */
        public void setIspublic(int ispublic) {
            this.ispublic = ispublic;
        }

        /**
         * @return The isfriend
         */
        public int getIsfriend() {
            return isfriend;
        }

        /**
         * @param isfriend The isfriend
         */
        public void setIsfriend(int isfriend) {
            this.isfriend = isfriend;
        }

        /**
         * @return The isfamily
         */
        public int getIsfamily() {
            return isfamily;
        }

        /**
         * @param isfamily The isfamily
         */
        public void setIsfamily(int isfamily) {
            this.isfamily = isfamily;
        }



        public String getImgUrl(){
            return  "http://farm" + getFarm()+ ".staticflickr.com/" + getServer() +
                    "/" + getId() + "_" + getSecret() +"_m"+ ".jpg";
        }



    }
   public class Photos {

        @Expose
        private int page;
        @Expose
        private int pages;
        @Expose
        private int perpage;
        @Expose
        private String total;
        @Expose
        private ArrayList<Photo> photo = new ArrayList<Photo>();

        /**
         *
         * @return
         *     The page
         */
        public int getPage() {
            return page;
        }

        /**
         *
         * @param page
         *     The page
         */
        public void setPage(int page) {
            this.page = page;
        }

        /**
         *
         * @return
         *     The pages
         */
        public int getPages() {
            return pages;
        }

        /**
         *
         * @param pages
         *     The pages
         */
        public void setPages(int pages) {
            this.pages = pages;
        }

        /**
         *
         * @return
         *     The perpage
         */
        public int getPerpage() {
            return perpage;
        }

        /**
         *
         * @param perpage
         *     The perpage
         */
        public void setPerpage(int perpage) {
            this.perpage = perpage;
        }

        /**
         *
         * @return
         *     The total
         */
        public String getTotal() {
            return total;
        }

        /**
         *
         * @param total
         *     The total
         */
        public void setTotal(String total) {
            this.total = total;
        }

        /**
         *
         * @return
         *     The photo
         */
        public ArrayList<Photo> getPhoto() {
            return photo;
        }

        /**
         *
         * @param photo
         *     The photo
         */
        public void setPhoto(ArrayList<Photo> photo) {
            this.photo = photo;
        }

    }
}