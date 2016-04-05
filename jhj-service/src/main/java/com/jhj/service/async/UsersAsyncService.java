package com.jhj.service.async;

import java.util.concurrent.Future;

public interface UsersAsyncService {

	Future<Boolean> allotAm(Long addrId);

	Future<Boolean> allotOrg(Long addrId);
	
}