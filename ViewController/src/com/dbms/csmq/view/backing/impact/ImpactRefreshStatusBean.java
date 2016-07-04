package com.dbms.csmq.view.backing.impact;


import com.dbms.csmq.view.backing.NMQ.NMQUtils;
import com.dbms.util.dml.DMLUtils;

import java.sql.CallableStatement;
import java.sql.Date;
import java.sql.Types;

import javax.faces.event.ActionEvent;

import oracle.adf.view.faces.bi.component.gauge.UIGauge;
import oracle.adf.view.rich.component.rich.RichPoll;
import oracle.adf.view.rich.component.rich.RichPopup;
import oracle.adf.view.rich.component.rich.layout.RichPanelGroupLayout;
import oracle.adf.view.rich.context.AdfFacesContext;

import oracle.jbo.server.DBTransaction;

import org.apache.myfaces.trinidad.event.PollEvent;


public class ImpactRefreshStatusBean {
    private RichPopup cntrlRefreshMatViewsPopup;
    private UIGauge cntrlGauge;
    private RichPanelGroupLayout cntrlPanel;
    private RichPoll cntrlTimer;

    public ImpactRefreshStatusBean() {
        super();
       System.out.println("START: ImpactRefreshStatusBean");
        
        getStatusFromDB ();
        System.out.println("END: ImpactRefreshStatusBean");
    }
    
    
    private String status;
    private Date submitTime;
    private Date startTime;
    private Date endTime;
    private int totalRecords;
    private int recordCountNow;
    
    
    private void getStatusFromDB () {
        
       // CALL PROC TO SAVE
       /*
       PROCEDURE get_impact_mv_progress(
         o_status OUT VARCHAR2,
         o_submit_time OUT DATE,
         o_start_time OUT DATE,
         o_end_time OUT DATE,
         o_total_records OUT PLS_INTEGER,
         o_record_count_now OUT PLS_INTEGER);
       */
       System.out.println("START getStatusFromDB" );
        String sql = "{call impact_pkg.get_impact_mv_progress(?,?,?,?,?,?)}";
        DBTransaction dBTransaction = DMLUtils.getDBTransaction();
        CallableStatement cstmt = dBTransaction.createCallableStatement(sql, DBTransaction.DEFAULT);

        try {
            cstmt.setString("o_status", "");
            cstmt.registerOutParameter("o_status", Types.NVARCHAR);
            
            cstmt.setDate("o_submit_time", null);
            cstmt.registerOutParameter("o_submit_time", Types.DATE);
            
            cstmt.setDate("o_start_time", null);
            cstmt.registerOutParameter("o_start_time", Types.DATE);
        
            cstmt.setDate("o_end_time", null);
            cstmt.registerOutParameter("o_end_time", Types.DATE);
        
            cstmt.setInt("o_total_records", -1);
            cstmt.registerOutParameter("o_total_records", Types.INTEGER);
        
            cstmt.setInt("o_record_count_now", -1);
            cstmt.registerOutParameter("o_record_count_now", Types.INTEGER);

            cstmt.executeUpdate();
            status = cstmt.getString("o_status");
            submitTime = cstmt.getDate("o_submit_time");
            startTime = cstmt.getDate("o_start_time");
            endTime = cstmt.getDate("o_end_time");
            totalRecords = cstmt.getInt("o_total_records");
            recordCountNow = cstmt.getInt("o_record_count_now");
            
            System.out.println("END getStatusFromDB" );
            
            cstmt.close();
            
            } 
        catch (Exception e) {
            e.printStackTrace();
            }
        }


    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setSubmitTime(Date submitTime) {
        this.submitTime = submitTime;
    }

    public Date getSubmitTime() {
        return submitTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setTotalRecords(int totalRecords) {
        this.totalRecords = totalRecords;
    }

    public int getTotalRecords() {
        return totalRecords;
    }

    public void setRecordCountNow(int recordCountNow) {
        this.recordCountNow = recordCountNow;
    }

    public int getRecordCountNow() {
        return recordCountNow;
    }
    
    public void showExportPopup(ActionEvent actionEvent) {
        RichPopup.PopupHints hints = new RichPopup.PopupHints();
        cntrlRefreshMatViewsPopup.show(hints);
    }

    public void setCntrlRefreshMatViewsPopup(RichPopup cntrlRefreshMatViewsPopup) {
        this.cntrlRefreshMatViewsPopup = cntrlRefreshMatViewsPopup;
    }

    public RichPopup getCntrlRefreshMatViewsPopup() {
        return cntrlRefreshMatViewsPopup;
    }

    public void refreshSearchList(ActionEvent actionEvent) {
        // refresh the views and turn on the polling
        NMQUtils.refreshImpactMVs();
        this.cntrlTimer.setInterval(5000);
    }

    public void pollTrigger(PollEvent pollEvent) {
        getStatusFromDB ();
        //AdfFacesContext.getCurrentInstance().addPartialTarget(cntrlPanel); 
        AdfFacesContext.getCurrentInstance().partialUpdateNotify(cntrlPanel);
        
    }

    public void setCntrlGauge(UIGauge cntrlGauge) {
        this.cntrlGauge = cntrlGauge;
    }

    public UIGauge getCntrlGauge() {
        return cntrlGauge;
    }

    public void setCntrlPanel(RichPanelGroupLayout cntrlPanel) {
        this.cntrlPanel = cntrlPanel;
    }

    public RichPanelGroupLayout getCntrlPanel() {
        return cntrlPanel;
    }

    public void setCntrlTimer(RichPoll cntrlTimer) {
        this.cntrlTimer = cntrlTimer;
    }

    public RichPoll getCntrlTimer() {
        return cntrlTimer;
    }
}
