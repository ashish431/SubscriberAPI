package com.eurowings.subscriber.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="UserSubscription")
public class User {

	@Id
	@Column(name = "email_id",nullable = false)
	private String emailId;
	@Column(name = "name",nullable = false)
	private String name;
	@Column(name = "mobile_no",nullable = false)
	private String mobileNo;
	@Column(name = "gdpr",nullable = false)
	private boolean gdpr;
	@Temporal(TemporalType.DATE)
	@Column(name = "subscription_date",nullable = false)
	private Date subscriptionDate;
	@Column(name = "newsletter_subcription",nullable = false)
	private boolean newsletterSubcribed;
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	public boolean isGdpr() {
		return gdpr;
	}
	public void setGdpr(boolean gdpr) {
		this.gdpr = gdpr;
	}
	public Date getSubscriptionDate() {
		return subscriptionDate;
	}
	public void setSubscriptionDate(Date subscriptionDate) {
		this.subscriptionDate = subscriptionDate;
	}
	
	public boolean isNewsletterSubcribed() {
		return newsletterSubcribed;
	}
	public void setNewsletterSubcribed(boolean newsletterSubcribed) {
		this.newsletterSubcribed = newsletterSubcribed;
	}

	@Override
	public String toString() {
		return "User [emailId=" + emailId + ", name=" + name + ", mobileNo=" + mobileNo + ", gdpr=" + gdpr
				+ ", subscriptionDate=" + subscriptionDate + ", newsletterSubcribed=" + newsletterSubcribed + "]";
	}

	
	
}
