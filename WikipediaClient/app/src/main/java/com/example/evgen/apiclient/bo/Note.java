package com.example.evgen.apiclient.bo;

import org.json.JSONObject;

/**
 * Created by User on 22.10.2014.
 */
public class Note extends JSONObjectWrapper {

  /*  private static final String TITLE = "title";
    private static final String CONTENT = "content";
    private static final String ID = "id";*/
  private String title;

    private String content;

    private String id;

   public Note(String jsonObject) {
        super(jsonObject);
   }

    public Note(JSONObject jsonObject) {
        super(jsonObject);
    }

    public String getTitle() {
        return getString("title");
    }

    public String getContent() {
        return getString("content");
    }

    public Long getId() {
        return getLong("id");
    }
   /*public String getTitle() {
       return title;
   }

    public Long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }*/

}
