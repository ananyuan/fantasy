package com.dream.db.model;

import java.io.Serializable;
import java.util.List;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "MAIL")
public class MailBean implements Serializable {

	private static final long serialVersionUID = -7316230428744780522L;

	public MailBean() {
    }
	
	@DatabaseField
	private String subject = "";

	@DatabaseField
	private String from = "";

	@DatabaseField
	private String fromUserName = "";

	@DatabaseField
	private String sendTime = "";

	@DatabaseField
	private String to = "";

	@DatabaseField
	private String receiveTime = "";

	@DatabaseField
	private String cc = "";

	@DatabaseField
	private String bcc = "";

	@DatabaseField(id = true)
	private String messageId = "";

	@DatabaseField
	private String receiver = "";

	@DatabaseField
	private boolean isOpen = false;

	@DatabaseField
	private boolean hasFile = false;
	
	private List<MailContent> contents;

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getFromUserName() {
		return fromUserName;
	}

	public void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
	}

	public String getSendTime() {
		return sendTime;
	}

	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getReceiveTime() {
		return receiveTime;
	}

	public void setReceiveTime(String receiveTime) {
		this.receiveTime = receiveTime;
	}

	public String getCc() {
		return cc;
	}

	public void setCc(String cc) {
		this.cc = cc;
	}

	public String getBcc() {
		return bcc;
	}

	public void setBcc(String bcc) {
		this.bcc = bcc;
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public boolean isOpen() {
		return isOpen;
	}

	public void setOpen(boolean isOpen) {
		this.isOpen = isOpen;
	}

	public boolean isHasFile() {
		return hasFile;
	}

	public void setHasFile(boolean hasFile) {
		this.hasFile = hasFile;
	}

	public List<MailContent> getContents() {
		return contents;
	}

	public void setContents(List<MailContent> contents) {
		this.contents = contents;
	}

}
