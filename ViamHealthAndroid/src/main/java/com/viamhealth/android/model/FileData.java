package com.viamhealth.android.model;

import android.net.Uri;

public class FileData {
	String id,user,name,description,download_url, mimeType;
	boolean checked=false;


	public FileData(String id, String user, String name, String description,
			String download_url, String mimeType) {
		super();
		this.id = id;
		this.user = user;
		this.name = name;
		this.description = description;
		this.download_url = download_url;
        this.mimeType = mimeType;
	}

    public Uri getUri() {
        try {
            return Uri.parse(this.download_url)
                    .buildUpon().appendQueryParameter("user", this.user)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDownload_url() {
		return download_url;
	}

	public void setDownload_url(String download_url) {
		this.download_url = download_url;
	}

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }
}
