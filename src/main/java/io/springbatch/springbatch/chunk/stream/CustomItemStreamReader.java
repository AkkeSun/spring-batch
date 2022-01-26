package io.springbatch.springbatch.chunk.stream;

import org.springframework.batch.item.*;

import java.util.List;

public class CustomItemStreamReader implements ItemStreamReader<String> {

    private List<String> items;
    private int index = -1;
    private boolean restart = false;

    public CustomItemStreamReader(List<String> items){
        this.items = items;
        this.index = 0;
    }

    // 데이터 로드
    @Override
    public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        String item = null;

        if(this.index < this.items.size()){
            item = this.items.get(index);
            index++;
            System.out.println(">>> ItemStreamReader : READ (DATA : " + item + " )");
        }

        if(this.index == 6 && !restart){
            throw new RuntimeException("Restart is required");
        }
        return item;
    }


    // 초기화 작업
    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {

        // 재시작이라면
        if(executionContext.containsKey("index")) {
            this.index = executionContext.getInt("index");
            this.restart = true;
            System.out.println(">>> ItemStreamReader : OPEN (RESTART)");
        // 최초실행이라면
        } else {
            this.index = 0;
            executionContext.put("index", index);
            System.out.println(">>> ItemStreamReader : OPEN (NEW START)");
        }
    }


    // 현재의 상태정보 실시간 저장
    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
        System.out.println(">>> ItemStreamReader : UPDATE");
        executionContext.put("index", index);
    }

    // Job 이 종료되면 호출
    @Override
    public void close() throws ItemStreamException {
        System.out.println(">>> ItemStreamReader : CLOSE");
    }
}
