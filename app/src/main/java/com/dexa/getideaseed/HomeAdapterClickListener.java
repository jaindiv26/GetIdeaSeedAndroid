package com.dexa.getideaseed;

/**
 * Created by Dev on 07/03/18.
 */

public interface HomeAdapterClickListener {
    void onEdit (ModelExplorer modelExplorer,int position);
    void onDelete (ModelExplorer modelExplorer);
    void onFeedback(ModelExplorer modelExplorer);
    void onNoResultFound(boolean result,boolean backUpListIsEmpty);
}
