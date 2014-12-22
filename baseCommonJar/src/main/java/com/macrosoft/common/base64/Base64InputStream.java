package com.macrosoft.common.base64;

import java.io.IOException;
import java.io.InputStream;

public class Base64InputStream extends InputStream {
	private InputStream inputStream;
	private int[] buffer;
	private int bufferCounter = 0;

	private boolean eof = false;

	public Base64InputStream(InputStream inputStream) {

		this.inputStream = inputStream;
	}

	public int read() throws IOException {
		if ((this.buffer == null) || (this.bufferCounter == this.buffer.length)) {
			if (this.eof) {
				return -1;
			}
			acquire();
			if (this.buffer.length == 0) {
				this.buffer = null;
				return -1;
			}
			this.bufferCounter = 0;
		}
		return this.buffer[(this.bufferCounter++)];
	}

	private void acquire() throws IOException {
		char[] four = new char[4];
		int i = 0;
		do {
			int b = this.inputStream.read();
			if (b == -1) {
				if (i != 0) {
					throw new IOException("Bad base64 stream");
				}
				this.buffer = new int[0];
				this.eof = true;
				return;
			}
			char c = (char) b;
			if (("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
					.indexOf(c) != -1) || (c == '='))
				four[(i++)] = c;
			else if ((c != '\r') && (c != '\n'))
				throw new IOException("Bad base64 stream");
		} while (i < 4);
		boolean padded = false;
		for (i = 0; i < 4; i++)
			if (four[i] != '=') {
				if (padded) {
					throw new IOException("Bad base64 stream");
				}
			} else if (!padded)
				padded = true;
		int l;
		if (four[3] == '=') {
			if (this.inputStream.read() != -1) {
				throw new IOException("Bad base64 stream");
			}
			this.eof = true;
			if (four[2] == '=')
				l = 1;
			else
				l = 2;
		} else {
			l = 3;
		}
		int aux = 0;
		for (i = 0; i < 4; i++) {
			if (four[i] != '=') {
				aux |= "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
						.indexOf(four[i]) << 6 * (3 - i);
			}
		}
		this.buffer = new int[l];
		for (i = 0; i < l; i++)
			this.buffer[i] = (aux >>> 8 * (2 - i) & 0xFF);
	}

	public void close() throws IOException {
		this.inputStream.close();
	}
}
