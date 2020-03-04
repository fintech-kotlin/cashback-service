package ru.tinkoff.fintech.listener

import com.fasterxml.jackson.databind.ObjectMapper
import mu.KotlinLogging
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.ClassRule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.core.io.Resource
import org.springframework.kafka.config.KafkaListenerEndpointRegistry
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory
import org.springframework.kafka.test.rule.EmbeddedKafkaRule
import org.springframework.kafka.test.utils.ContainerTestUtils
import org.springframework.kafka.test.utils.KafkaTestUtils
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.junit4.SpringRunner
import ru.tinkoff.fintech.client.CardServiceClient
import ru.tinkoff.fintech.client.ClientService
import ru.tinkoff.fintech.client.LoyaltyServiceClient
import ru.tinkoff.fintech.db.repository.LoyaltyPaymentRepository
import ru.tinkoff.fintech.model.Card
import ru.tinkoff.fintech.model.Client
import ru.tinkoff.fintech.model.LoyaltyProgram
import ru.tinkoff.fintech.service.notification.NotificationService
import java.nio.file.Files
import java.nio.file.Paths

private const val RECEIVER_TOPIC = "testTopic"

@RunWith(SpringRunner::class)
@SpringBootTest
@DirtiesContext
internal class MyTestClass {

    @Value("classpath:/transaction.json")
    lateinit var transactionResource: Resource
    @Value("classpath:/card.json")
    lateinit var cardResource: Resource
    @Value("classpath:/client.json")
    lateinit var clientResource: Resource
    @Value("classpath:/loyalty.json")
    lateinit var loyaltyResource: Resource

    @MockBean
    lateinit var clientService: ClientService
    @MockBean
    lateinit var cardService: CardServiceClient
    @MockBean
    lateinit var loyaltyService: LoyaltyServiceClient
    @MockBean
    lateinit var notificationService: NotificationService
    @Autowired
    lateinit var objectMapper: ObjectMapper
    @Autowired
    private val loyaltyPaymentRepository: LoyaltyPaymentRepository? = null
    @Autowired
    private val kafkaListenerEndpointRegistry: KafkaListenerEndpointRegistry? = null

    private var kafkaTemplate: KafkaTemplate<String, String>? = null

    private lateinit var card: Card
    private lateinit var client: Client
    private lateinit var transaction: String
    private lateinit var loyalty: LoyaltyProgram

    @Before
    @Throws(Exception::class)
    fun setUp() {
        setMocksValues()
        startKafka()
    }

    @Test
    fun testReceive() {
        Mockito.`when`(cardService.getCard(anyString())).thenReturn(card)
        Mockito.`when`(clientService.getClient(anyString())).thenReturn(client)
        Mockito.`when`(loyaltyService.getLoyaltyProgram(anyString())).thenReturn(loyalty)
        kafkaTemplate!!.send(RECEIVER_TOPIC, transaction)
        log.info("Transaction have sent='{}'", transaction)
        Thread.sleep(15000) // I'm so sorry, that u see this code.
        val payments = loyaltyPaymentRepository!!.findAll()
        assertEquals(1, payments.size)
        val payment = payments[0]
        assertEquals(1, payment.id)
        assertEquals("60c1d3eb-5bbe-11ea-9b64-6d52d46e8675", payment.cardId)
        assertEquals("539f0ed9-c65e-47fb-b62e-ec10ad99ad68", payment.transactionId)
        assertEquals(63.0, payment.value, 0.0)
        embeddedKafka.embeddedKafka.kafkaServers.forEach { it.shutdown() }
    }

    private fun setMocksValues() {
        transaction = resourceToString(transactionResource)
        card = unmarshalResource(cardResource, Card::class.java)
        client = unmarshalResource(clientResource, Client::class.java)
        loyalty = unmarshalResource(loyaltyResource, LoyaltyProgram::class.java)
    }

    private fun startKafka() {
        val senderProperties = KafkaTestUtils.senderProps(embeddedKafka.embeddedKafka.brokersAsString)
        val producerFactory: ProducerFactory<String, String> = DefaultKafkaProducerFactory(senderProperties)
        kafkaTemplate = KafkaTemplate(producerFactory)
        for (messageListenerContainer in kafkaListenerEndpointRegistry!!.listenerContainers) {
            ContainerTestUtils.waitForAssignment(messageListenerContainer, embeddedKafka.embeddedKafka.partitionsPerTopic)
        }
    }

    private fun <T> unmarshalResource(resource: Resource, clazz: Class<T>): T {
        val resourceAsString = resourceToString(resource)
        return objectMapper.readValue(resourceAsString, clazz)
    }

    private fun resourceToString(resource: Resource) =
        Files.readAllLines(Paths.get(resource.uri))[0]

    companion object {
        @ClassRule
        @JvmField
        val embeddedKafka = EmbeddedKafkaRule(1, false, RECEIVER_TOPIC)
        private val log = KotlinLogging.logger {}
    }
}