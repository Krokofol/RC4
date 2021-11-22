package lab

import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*
import kotlin.random.Random

@ExperimentalUnsignedTypes
class RC4 {

    private val scanner = Scanner(System.`in`)
    private val random = Random(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC))

    fun start(args: Array<String>) {
        when(args.size) {
            1 -> code(args[0].map{ it.code.toUByte() })
            2 -> decode(args[0].map { it.code.toUByte() }, args[1].map { it.code.toUByte() })
            else -> {
                println("Wrong args")
                startWithoutArgs()
            }
        }
    }

    fun startWithoutArgs() {
        var action: Action? = null
        while (action == null) {
            println("Enter code/decode")
            try {
                action = Action.valueOf(scanner.nextLine().uppercase(Locale.getDefault()))
            } catch (e: Exception) {
                println("Try again")
            }
        }
        when (action) {
            Action.CODE -> {
                println("Enter your text")
                val text = scanner.nextLine()
                code(text.map { it.code.toUByte() })
            }
            Action.DECODE -> {
                var code: String? = null
                var key: String? = null
                while (code == null || key == null || code.length != key.length) {
                    if (code != null && key != null) {
                        println("Key and code length are not equal, try again")
                    }
                    println("Enter code")
                    code = scanner.nextLine()
                    println("Enter key")
                    key = scanner.nextLine()
                }
                decode(code.map { it.code.toUByte() }, key.map { it.code.toUByte() })
            }
        }
    }

    private fun code(byteText: List<UByte>) {
        val byteKey = Array(byteText.size) {
            random.nextInt().toUByte()
        }
        val byteCode = Array(byteText.size) { position ->
            byteText[position] xor byteKey[position]
        }
        println("key : \"${String(byteKey.map { it.toInt().toChar() }.toCharArray())}\"")
        println("code : \"${String(byteCode.map { it.toInt().toChar() }.toCharArray())}\"")
    }

    private fun decode(byteCode: List<UByte>, byteKey: List<UByte>) {
        val byteText = Array(byteCode.size) { position ->
            byteKey[position] xor (byteCode[position])
        }
        println("text : \"${String(byteText.map { it.toInt().toChar() }.toCharArray())}\"")
    }

    enum class Action {
        CODE,
        DECODE;
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            if (args.isNotEmpty()) {
                RC4().start(args)
            } else {
                RC4().startWithoutArgs()
            }
        }
    }
}