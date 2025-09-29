package co.flyingtoaster.mind.mcp.tools

import org.springframework.ai.support.ToolCallbacks
import org.springframework.ai.tool.ToolCallback
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ToolConfiguration {

    @Bean
    fun toolCallbacks(
        reminderToolService: ReminderToolService
    ) : List<ToolCallback> {
        return ToolCallbacks.from(
            reminderToolService
        ).asList()
    }
}