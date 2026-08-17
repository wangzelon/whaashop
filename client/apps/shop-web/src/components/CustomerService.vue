<script setup lang="ts">
import { nextTick, ref } from 'vue'

type ChatMessage = { role: 'user' | 'assistant'; content: string; error?: boolean }
const open = ref(false)
const input = ref('')
const sending = ref(false)
const messages = ref<ChatMessage[]>([
  { role: 'assistant', content: '您好，我是智能客服小橙。可以问我订单、发货、收货和商城服务，也可以陪您聊聊天。' }
])
const messageList = ref<HTMLElement>()
const suggestions = ['我的订单到哪了？', '怎么确认收货？', '退换货规则是什么？']
const conversationId = getConversationId()

function getConversationId() {
  const key = 'customer-service-conversation-id'
  let value = sessionStorage.getItem(key)
  if (!value) {
    value = crypto.randomUUID().replaceAll('-', '')
    sessionStorage.setItem(key, value)
  }
  return value
}

async function scrollToBottom() {
  await nextTick()
  messageList.value?.scrollTo({ top: messageList.value.scrollHeight, behavior: 'smooth' })
}

async function send(text = input.value) {
  const question = text.trim()
  if (!question || sending.value) return
  input.value = ''
  messages.value.push({ role: 'user', content: question })
  const answer: ChatMessage = { role: 'assistant', content: '' }
  messages.value.push(answer)
  sending.value = true
  await scrollToBottom()
  try {
    const baseUrl = import.meta.env.VITE_API_BASE_URL ?? 'http://localhost:8080/api/v1'
    const token = localStorage.getItem('token')
    const response = await fetch(`${baseUrl}/customer-service/chat`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json', ...(token ? { Authorization: `Bearer ${token}` } : {}) },
      body: JSON.stringify({ message: question, conversationId })
    })
    if (!response.ok) throw new Error(`客服暂时不可用（${response.status}）`)
    if (!response.body) throw new Error('浏览器不支持流式回复')
    const reader = response.body.getReader()
    const decoder = new TextDecoder()
    let buffer = ''
    while (true) {
      const { done, value } = await reader.read()
      buffer += decoder.decode(value, { stream: !done }).replace(/\r\n/g, '\n')
      const events = buffer.split('\n\n')
      buffer = events.pop() ?? ''
      for (const event of events) {
        answer.content += event.split('\n').filter(line => line.startsWith('data:'))
          .map(line => line.slice(5).trimStart()).join('\n')
      }
      await scrollToBottom()
      if (done) break
    }
    if (buffer.startsWith('data:')) answer.content += buffer.slice(5).trimStart()
  } catch (error) {
    answer.content = error instanceof Error ? error.message : '客服暂时不可用，请稍后再试。'
    answer.error = true
  } finally {
    sending.value = false
    await scrollToBottom()
  }
}
</script>

<template>
  <div class="customer-service">
    <transition name="chat-pop">
      <section v-if="open" class="chat-window" aria-label="智能客服窗口">
        <header class="chat-header">
          <div class="bot-avatar">橙</div>
          <div><strong>智能客服小橙</strong><small><i></i> 在线为您服务</small></div>
          <button aria-label="关闭客服" @click="open=false">×</button>
        </header>
        <div ref="messageList" class="chat-messages" aria-live="polite">
          <div v-for="(message,index) in messages" :key="index" class="chat-row" :class="message.role">
            <span v-if="message.role==='assistant'" class="mini-avatar">橙</span>
            <p :class="{error:message.error}">{{message.content || '正在思考…'}}</p>
          </div>
        </div>
        <div v-if="messages.length===1" class="quick-questions">
          <button v-for="item in suggestions" :key="item" @click="send(item)">{{item}}</button>
        </div>
        <form class="chat-input" @submit.prevent="send()">
          <textarea v-model="input" rows="1" maxlength="500" placeholder="请输入您想咨询的问题…" @keydown.enter.exact.prevent="send()"></textarea>
          <button :disabled="sending||!input.trim()" aria-label="发送消息">➤</button>
        </form>
        <div class="chat-tip">智能回复仅供参考，订单信息以“我的订单”为准</div>
      </section>
    </transition>
    <button class="service-fab" :class="{active:open}" :aria-expanded="open" :aria-label="open?'关闭智能客服':'打开智能客服'" @click="open=!open">
      <span v-if="open" class="fab-close">×</span>
      <span v-else class="robot-icon" aria-hidden="true">
        <i class="robot-antenna"></i>
        <i class="robot-head"><em></em><em></em><small></small></i>
        <i class="robot-ear left"></i><i class="robot-ear right"></i>
      </span>
      <b v-if="!open">智能客服</b>
    </button>
  </div>
</template>
