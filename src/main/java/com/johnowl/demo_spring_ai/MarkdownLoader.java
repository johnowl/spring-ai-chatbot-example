package com.johnowl.demo_spring_ai;

import org.springframework.ai.reader.markdown.MarkdownDocumentReader;
import org.springframework.ai.reader.markdown.config.MarkdownDocumentReaderConfig;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

@Component
public class MarkdownLoader implements InitializingBean {

    private final VectorStore vectorStore;

    public MarkdownLoader(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    private final MarkdownDocumentReaderConfig config = MarkdownDocumentReaderConfig.builder().build();

    @Override
    public void afterPropertiesSet() throws Exception {
        final var resolver = new PathMatchingResourcePatternResolver();
        final var markdownFiles = resolver.getResources("*.md");

        for (var file : markdownFiles) {
            final var reader = new MarkdownDocumentReader(file, config);
            final var documents = reader.read();
            vectorStore.add(documents);
        }
    }
}
