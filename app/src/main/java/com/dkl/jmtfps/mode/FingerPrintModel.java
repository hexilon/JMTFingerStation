package com.dkl.jmtfps.mode;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 指纹信息抽象类
 * 
 * @author dkl
 * @since 2015-2-4
 */
public class FingerPrintModel implements Parcelable {
	/*
	 * 每个指纹应该保存的数据文件个数，也是每次录取指纹的时录取的次数
	 */
	public static final int FP_DATA_ITEM = 10;
	public int fp_id;
	/*
	 * 指纹名称
	 */
	public String fp_name;
	/*
	 * 指纹索引，每个指纹都有个数据文件，他们对应的fp_data_index都相同
	 */
	public String fp_data_index;

	public FingerPrintModel() {

	}

	@Override
	public int describeContents() {

		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {

		dest.writeInt(fp_id);
		dest.writeString(fp_name);
		dest.writeString(fp_data_index);
	}

	public static final Parcelable.Creator<FingerPrintModel> CREATOR = new Parcelable.Creator<FingerPrintModel>() {

		@Override
		public FingerPrintModel createFromParcel(Parcel source) {
			FingerPrintModel model = new FingerPrintModel();
			model.fp_id = source.readInt();
			model.fp_name = source.readString();
			model.fp_data_index = source.readString();
			return model;
		}

		@Override
		public FingerPrintModel[] newArray(int size) {
			return new FingerPrintModel[size];
		}
	};

}
