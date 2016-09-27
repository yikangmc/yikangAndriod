package com.yikang.app.yikangserver.photo;

import java.io.Serializable;

/**
 * 一个图片对象
 * 
 * @author Administrator
 * 
 */
public class ImageItem implements Serializable,Comparable<ImageItem> {
	public String imageId;
	public String thumbnailPath;
	public String imagePath;
	public boolean isSelected = false;

	@Override
	public int compareTo(ImageItem another) {
		return 0;
	}
}
