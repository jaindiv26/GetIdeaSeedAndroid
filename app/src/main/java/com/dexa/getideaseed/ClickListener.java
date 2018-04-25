package com.dexa.getideaseed;

/**
 * Created by Dev on 02/03/18.
 */

public interface ClickListener {

    void onClick(ModelExplorer modelExplorer);
    void onClick(ModelFeedback modelFeedback, String comment);
    void onNoResultFound(boolean result,boolean backUpListIsEmpty);
    void onLightBulbClicked(ModelExplorer modelExplorer);
}
