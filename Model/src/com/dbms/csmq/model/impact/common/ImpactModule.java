package com.dbms.csmq.model.impact.common;

import oracle.jbo.ApplicationModule;
// ---------------------------------------------------------------------
// ---    File generated by Oracle ADF Business Components Design Time.
// ---    Sat Jul 30 10:59:15 IST 2016
// ---------------------------------------------------------------------
public interface ImpactModule extends ApplicationModule {
    void onPreviousVerImpactSearch(String searchLevelStr, String searchTermStr, String searchCodeStr);

    void loadPrevVersionCurrentNFurteMQDetails(Long dictContentId);
}

