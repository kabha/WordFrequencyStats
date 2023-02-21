package com.cylo.response;

import java.io.Serializable;

public class BaseResponse implements Serializable {


		private static final long serialVersionUID = 1L;
		private RequestStatus requestStatus;

		public BaseResponse() {
		}

		public BaseResponse(RequestStatus requestStatus) {
			super();
			this.requestStatus = requestStatus;
		}

		public RequestStatus getRequestStatus() {
			return requestStatus;
		}

		public void setRequestStatus(RequestStatus requestStatus) {
			this.requestStatus = requestStatus;
		}

		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("BaseResponse [requestStatus=").append(requestStatus).append("]");
			return builder.toString();
		}
	

}
