package io.springbatch.springbatch.listener.chunk.listener;

import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.scope.context.ChunkContext;

public class ChunkListenerTest implements ChunkListener {


    @Override
    // 트랜젝션이 시작되기 전 호출 
    public void beforeChunk(ChunkContext chunkContext) {
        String stepName = chunkContext.getStepContext().getStepName();
        System.err.println(">> [CHUNK] " + "\""+ stepName + "\"" + " IS STARTED");
    }

    @Override
    // Chunk 가 커밋된 후 호출
    public void afterChunk(ChunkContext chunkContext) {
        String exitCode = chunkContext.getStepContext().getStepExecution().getExitStatus().getExitCode();
        System.err.println(">> [CHUNK] STATUS : " + exitCode);
    }

    @Override
    // 오류 발생 및 롤백이 되면 호출
    public void afterChunkError(ChunkContext chunkContext) {

    }
}
