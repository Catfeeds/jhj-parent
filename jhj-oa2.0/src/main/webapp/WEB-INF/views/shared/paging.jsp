<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	import="com.github.pagehelper.PageInfo"
    import="com.meijia.utils.PageUtil"
    import="com.jhj.common.ConstantOa"
    import="org.apache.taglibs.standard.tag.common.core.UrlSupport"%>

<%
	String urlAddress=request.getParameter("urlAddress");
	String pageModelName= request.getParameter("pageModelName");
	Integer pageCount= ConstantOa.DEFAULT_PAGE_SIZE;
	if (request.getParameter("pageCount")!=null &&
	    !request.getParameter("pageCount").isEmpty()) {
		pageCount = Integer.valueOf(request.getParameter("pageCount"));
	}
%>

<div class="row">
<%
	if(pageModelName==null || pageModelName.isEmpty()){%>
		未获取到分页标识！
	<%}
	else if(urlAddress==null || urlAddress.isEmpty()){%>
		未获取到url地址！
	<%}
	else if(pageCount<10){%>
		分页数量不能小于10！
	<%}

	PageInfo pageInfo = (PageInfo)request.getAttribute(pageModelName);
	if(pageInfo==null){%>
		未获取到分页内容！
	<%}
	else{%>

		<div class="col-md-5 col-sm-12">
			<div class="dataTables_info">(每页显示<%=pageInfo.getPageSize() %>条，共 <%=pageInfo.getTotal() %> 条记录)</div>
		</div>

		<div class="col-md-7 col-sm-12">
			<div class="dataTables_paginate paging_bootstrap">
		   		<ul class="pagination">

		<%String queryString=request.getQueryString();
		urlAddress = UrlSupport.resolveUrl(urlAddress, null, pageContext);
		if (pageInfo.isHasPreviousPage()) {
            String preUrl = PageUtil.resolveUrl(urlAddress, queryString, pageInfo.getPageNum() - 1, null);%>
            <li class="prev">
				<a href="<%=preUrl%>">上一页</a>
			</li>
        <%}
		else{%>
			<li class="prev disabled">
				<span>上一页</span>
			</li>
		<%}
		if(pageInfo.getPages()<=pageCount){
        	for(int i=1;i<=pageInfo.getPages();i++){
        		if(i==pageInfo.getPageNum()){%>
        			<li class="active"><a href="#"><%=i %></a></li>
        		<%}
        		else{
        			String pageUrl  = PageUtil.resolveUrl(urlAddress, queryString, i, null);%>
                    <li><a href="<%=pageUrl%>"><%=i %></a></li>
                <%}
        	}
        }
		else{
        	for(int i=1;i<=pageCount;i++){
        		if(pageInfo.getPageNum()<=pageCount/2){
        			if(i==pageInfo.getPageNum()){%>
        				<li class="active"><a href="#"><%=i %></a></li>
        			<%}
        			else if(pageCount-i>2){
        				String pageUrl  = PageUtil.resolveUrl(urlAddress, queryString, i, null);%>
                        <li><a href="<%=pageUrl%>"><%=i %></a></li>
    				<%}
        			else if(i==pageCount-2){%>
        				<span>...</span>
        			<%}
        			else{
    					int pageNo=pageInfo.getPages()-(pageCount-i);
    					String pageUrl  = PageUtil.resolveUrl(urlAddress, queryString, pageNo, null);%>
                        <li><a href="<%=pageUrl%>"><%=pageNo %></a></li>
        			<%}
        		}
        		else if(pageInfo.getPages()-pageInfo.getPageNum()<pageCount/2){
        			if(i<3){
        				String pageUrl  = PageUtil.resolveUrl(urlAddress, queryString, i, null);%>
                        <li><a href="<%=pageUrl%>"><%=i %></a></li>
        			<%}
        			else if(i==3){%>
        				<span>...</span>
        			<%}
        			else{
        				int pageNo=pageInfo.getPages()-(pageCount-i);
        				if(pageNo==pageInfo.getPageNum()){%>
        					<li class="active"><a href="#"><%=pageNo %></a></li>
        				<%}
        				else{
        					String pageUrl  = PageUtil.resolveUrl(urlAddress, queryString, pageNo, null);%>
                            <li><a href="<%=pageUrl%>"><%=pageNo %></a></li>
        				<%}
        			}
        		}
        		else{
        			if(i<3){
        				String pageUrl  = PageUtil.resolveUrl(urlAddress, queryString, i, null);%>
                        <li><a href="<%=pageUrl%>"><%=i %></a></li>
        			<%}
        			else if(i==3 || i==pageCount-2){%>
        				<span>...</span>
        			<%}
        			else if(i>pageCount-2){
        				int pageNo=pageInfo.getPages()-(pageCount-i);
        				String pageUrl  = PageUtil.resolveUrl(urlAddress, queryString, pageNo, null);%>
                        <li><a href="<%=pageUrl%>"><%=pageNo %></a></li>
        			<%}
        			else{
        				if(pageCount%2==0)
        				{
            				if(i==pageCount/2){%>
            					<li class="active"><a href="#"><%=pageInfo.getPageNum() %></a></li>
            				<%}
            				else if(i<pageCount/2){
            					int pageNo=pageInfo.getPageNum()-(pageCount/2-i);
                				String pageUrl  = PageUtil.resolveUrl(urlAddress, queryString, pageNo, null);%>
                                <li><a href="<%=pageUrl%>"><%=pageNo %></a></li>
            				<%}
            				else{
            					int pageNo=pageInfo.getPageNum()+(i-pageCount/2);
                				String pageUrl  = PageUtil.resolveUrl(urlAddress, queryString, pageNo, null);%>
                                <li><a href="<%=pageUrl%>"><%=pageNo %></a></li>
            				<%}
        				}
        				else{
        					if(i==(pageCount+1)/2){%>
            					<li class="active"><a href="#"><%=pageInfo.getPageNum() %></a></li>
            				<%}
        					else if(i<(pageCount+1)/2){
            					int pageNo=pageInfo.getPageNum()-((pageCount+1)/2-i);
                				String pageUrl  = PageUtil.resolveUrl(urlAddress, queryString, pageNo, null);%>
                                <li><a href="<%=pageUrl%>"><%=pageNo %></a></li>
            				<%}
            				else{
            					int pageNo=pageInfo.getPageNum()+(i-(pageCount+1)/2);
                				String pageUrl  = PageUtil.resolveUrl(urlAddress, queryString, pageNo, null);%>
                                <li><a href="<%=pageUrl%>"><%=pageNo %></a></li>
            				<%}
        				}
        			}
        		}
        	}
        }
		if (pageInfo.isHasNextPage()) {
            String nextUrl  = PageUtil.resolveUrl(urlAddress, queryString, pageInfo.getPageNum() + 1, null);%>
            <li class="next">
				<a href="<%=nextUrl %>">下一页</a>
			</li>
        <%}
        else{%>
        	<li class="next disabled">
				<span>下一页</span>
			</li>
        <%}%>
        		</ul>
			</div>
		</div>

	<%}%>
</div>