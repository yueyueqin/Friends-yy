package de.greenrobot.daoexample;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table "RECORD".
 */
public class Record {

    private Long id;
    /** Not-null value. */
    private String type;
    private String image;
    /** Not-null value. */
    private String user_id;
    private String content;
    private java.util.Date add_time;
    /** Not-null value. */
    private String object_id;
    private String parent_id;

    public Record() {
    }

    public Record(Long id) {
        this.id = id;
    }

    public Record(Long id, String type, String image, String user_id, String content, java.util.Date add_time, String object_id, String parent_id) {
        this.id = id;
        this.type = type;
        this.image = image;
        this.user_id = user_id;
        this.content = content;
        this.add_time = add_time;
        this.object_id = object_id;
        this.parent_id = parent_id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /** Not-null value. */
    public String getType() {
        return type;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setType(String type) {
        this.type = type;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    /** Not-null value. */
    public String getUser_id() {
        return user_id;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public java.util.Date getAdd_time() {
        return add_time;
    }

    public void setAdd_time(java.util.Date add_time) {
        this.add_time = add_time;
    }

    /** Not-null value. */
    public String getObject_id() {
        return object_id;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setObject_id(String object_id) {
        this.object_id = object_id;
    }

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

}
