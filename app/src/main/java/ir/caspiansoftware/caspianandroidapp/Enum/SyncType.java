package ir.caspiansoftware.caspianandroidapp.Enum;

import ir.caspiansoftware.caspianandroidapp.R;

public enum SyncType {
    PERSON (R.string.sync_person_question, R.string.sync_person_title, R.string.sync_person_success_message),
    KALA(R.string.sync_kala_question, R.string.sync_kala_title, R.string.sync_kala_success_message),
    KALA_PHOTO (R.string.sync_kala_photo_question, R.string.sync_kala_photo_title, R.string.sync_kalaPhoto_success_message);

    private int syncQuestion;
    private int syncTitle;
    private int syncSuccess;

    SyncType(int syncQuestion, int syncTitle, int syncSuccess) {
        this.syncQuestion = syncQuestion;
        this.syncTitle = syncTitle;
        this.syncSuccess = syncSuccess;
    }

    public int getSyncQuestion() {
        return syncQuestion;
    }

    public int getSyncTitle() {
        return syncTitle;
    }

    public int getSyncSuccess() {
        return syncSuccess;
    }
}
