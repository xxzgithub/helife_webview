package com.hilife.webview.model.js;

public class JSRequestJoinParam extends BaseJSParam {

	/**
	 *
	 */
	private static final long serialVersionUID = 770008678438006733L;

    private String groupID;
    private String groupType;
    private Integer joinType;

    public String getGroupID() {
        return groupID;
    }

    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }

    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }

    public Integer getJoinType() {
        return joinType;
    }

    public void setJoinType(Integer joinType) {
        this.joinType = joinType;
    }
}
