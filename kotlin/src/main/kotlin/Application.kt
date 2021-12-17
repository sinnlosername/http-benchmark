import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.util.cio.*
import io.ktor.utils.io.*
import java.io.File
import java.util.concurrent.ThreadLocalRandom

const val DATA_DIR = "../data"

fun main() {
    embeddedServer(Netty, port = 4000, host = "0.0.0.0") {
        routing {
            get("/file/{file}") {
                call.respondBytesWriter {
                    getFile(call.parameters["file"]!!).readChannel().copyTo(this)
                }
            }

            post("/file") {
                val newFileName = ThreadLocalRandom.current().nextLong().toString()

                getFile(newFileName).writeChannel().use {
                    call.receiveChannel().copyTo(this)
                }
            }

            get("/static") {
                call.respondText("This is a static text!")
            }
        }
    }.start(wait = true)
}

fun getFile(name: String) = File(DATA_DIR, name)