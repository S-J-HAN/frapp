/* Team: frapp
 * IT Project Semester 2, 2019
 */

package com.itproject.frapp;

// Class containing the details of a comment
public class Comment {

    private String op;
    private String dateTime;
    private String text;

    public Comment() {
    }

    public Comment(String op, String dateTime, String text) {
        this.op = op;
        this.dateTime = dateTime;
        this.text = text;
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

    public String getOp() {
        return this.op;
    }

    public String getDateTime() {
        return this.dateTime;
    }

    public String getText() {
        return this.text;
    }

    public String toString() {
        return this.op + this.dateTime + this.text;
    }

}
