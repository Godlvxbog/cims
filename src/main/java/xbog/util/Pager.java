package xbog.util;

/**
 * Created by SuperS on 16/2/24.
 */
public class Pager {
    private int pageSize;//ÿҳ��ʾ��������
    private int currentPage;//��ǰҳ��
    private int totalRecord;//����������
    private int totalPage;//��ҳ����

    public int getTotalPage() {
        return totalPage;
    }

    //��ҳ
    private int firstPage;
    //ĩҳ
    private int lastPage;
    //��һҳ
    private int prePage;
    //��һҳ
    private int nextPage;

    public Pager() {
    }

    public Pager(int currentPage, int pageSize, int totalRecord) {
        this.pageSize = pageSize;
        this.totalRecord = totalRecord;
        this.totalPage = calculateTotalPage();
        //������� ����� ҳ������ �Ƿ񳬳����ҳ����ֵ ����������Ϊ���ҳ������
        this.currentPage = currentPage > totalPage ? totalPage : currentPage;
    }

    private int calculateTotalPage() {
        this.totalPage = this.totalRecord / this.pageSize;
        if (totalRecord % pageSize != 0) {
            this.totalPage = this.totalPage + 1;
        }
        return totalPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getCurrentPage() {
        return this.currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getTotalRecord() {
        return totalRecord;
    }

    public void setTotalRecord(int totalRecord) {
        this.totalRecord = totalRecord;
    }

    public int getFirstPage() {
        this.firstPage = 1;
        return this.firstPage;
    }

    public int getLastPage() {
        this.lastPage = totalPage;
        return lastPage;
    }

    public int getPrePage() {
        if (this.currentPage <= 1) {
            this.prePage = -1;
        }else {
            this.prePage = this.currentPage - 1;
        }
        return this.prePage;
    }

    public int getNextPage(){
        if(this.currentPage >= totalPage){
            this.nextPage = -1;
        }else {
            this.nextPage = this.currentPage + 1;
        }
        return this.nextPage;
    }

    public int getStartIndex(){
        return (currentPage-1)*pageSize;
    }
}
