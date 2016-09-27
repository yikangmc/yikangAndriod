package com.yikang.app.yikangserver.bean;

/**
 * 服务器版本更新
 * 
 * @author Administrator
 *
 */
public class UpdateInfoRes {
	public UpdateData data;

	public class UpdateData {
		
		public String downloadLink;
		public String appName;
		public String package_id;
		public String package_name;
		public int forcedToUpdate;
		public String updateInfo;
		public String updateVersion;
		public String versionCode;


		public String getDownloadLink() {
			return downloadLink;
		}

		public void setDownloadLink(String downloadLink) {
			this.downloadLink = downloadLink;
		}

		public int getForcedToUpdate() {
			return forcedToUpdate;
		}

		public void setForcedToUpdate(int forcedToUpdate) {
			this.forcedToUpdate = forcedToUpdate;
		}

		public String getUpdateInfo() {
			return updateInfo;
		}

		public void setUpdateInfo(String updateInfo) {
			this.updateInfo = updateInfo;
		}

		public String getUpdateVersion() {
			return updateVersion;
		}

		public void setUpdateVersion(String updateVersion) {
			this.updateVersion = updateVersion;
		}
	}

}
