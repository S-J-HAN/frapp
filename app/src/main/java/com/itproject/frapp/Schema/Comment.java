/* Team: frapp
 * IT Project Semester 2, 2019
 */

package com.itproject.frapp.Schema;

// Class containing the details of a comment
public class Comment {

    private String op;
    private String dateTime;
    private String text;
    private String id;

    public Comment() {
    }

    public Comment(String op, String dateTime, String text, String id) {
        this.op = op;
        this.dateTime = dateTime;
        this.text = text;
        this.id = id;
    }

    public void setOp(String op) {
        this.op = op;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOp() {
        return this.op;
    }

    public String getDateTime() {
        return this.dateTime;
    }

    public String getText() {
        return this.text;
    }

    public String getId() {
        return this.id;
    }

    public String toString() {
        return this.op + this.dateTime + this.text;
    }

}
