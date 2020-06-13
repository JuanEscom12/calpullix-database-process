package com.calpullix.db.process.twitter.service;

import java.io.IOException;

public interface TwitterProcessService {

	void processMessages(String pathFileMessages, String pathFileLabels, String pathCloudWordsImage);

	void processTwitter(String pathFile, String fileName);

	void watchTwitterMessagesFiles() throws IOException, InterruptedException;

	void watchTwitterFiles() throws IOException, InterruptedException;

	void setPercentages() throws IOException;
	
}
