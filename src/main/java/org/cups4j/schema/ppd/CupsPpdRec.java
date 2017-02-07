package org.cups4j.schema.ppd;

public class CupsPpdRec {

    byte[] ppdMd5;
    boolean isUpdated = false;
    PpdUiList stdList;
    PpdUiList extraList;
    PpdServiceInfo ppdServiceInfo;

    public CupsPpdRec() {
        byte[] md5 = {};
        ppdMd5 = md5;
    }

    public boolean getIsUpdated() {
        return isUpdated;
    }

    public byte[] getPpdMd5() {
        return ppdMd5;
    }

    public PpdUiList getStdList() {
        return stdList;
    }

    public PpdUiList getExtraList() {
        return extraList;
    }

    public PpdServiceInfo getPpdServiceInfo() {
        return ppdServiceInfo;
    }

    public CupsPpdRec deepCloneUILists() {
        CupsPpdRec rec = new CupsPpdRec();
        if (stdList != null) {
            rec.stdList = new PpdUiList();
            for (PpdSectionList list : stdList) {
                rec.stdList.add(list.deepClone());
            }
        }
        if (extraList != null) {
            rec.extraList = new PpdUiList();
            for (PpdSectionList list : extraList) {
                rec.extraList.add(list.deepClone());
            }
        }
        rec.ppdServiceInfo = new PpdServiceInfo();

        return rec;
    }


}
