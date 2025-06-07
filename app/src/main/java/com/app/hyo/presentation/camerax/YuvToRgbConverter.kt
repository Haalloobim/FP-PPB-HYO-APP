import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageFormat
import android.graphics.Rect
import android.media.Image
import android.renderscript.*
import java.nio.ByteBuffer


class YuvToRgbConverter(context: Context) {
    private val rs = RenderScript.create(context)
    private val scriptYuvToRgb = ScriptIntrinsicYuvToRGB.create(rs, Element.U8_4(rs))

    private var yuvBuffer: ByteBuffer? = null
    private var inputAllocation: Allocation? = null
    private var outputAllocation: Allocation? = null
    private var width = -1
    private var height = -1

    @Synchronized
    fun yuvToRgb(image: Image, output: Bitmap) {
        if (image.format != ImageFormat.YUV_420_888) {
            throw IllegalArgumentException("Invalid image format")
        }

        val yBuffer = image.planes[0].buffer
        val uBuffer = image.planes[1].buffer
        val vBuffer = image.planes[2].buffer

        val ySize = yBuffer.remaining()
        val uSize = uBuffer.remaining()
        val vSize = vBuffer.remaining()

        if (yuvBuffer == null || yuvBuffer!!.capacity() < ySize + uSize + vSize) {
            yuvBuffer = ByteBuffer.allocateDirect(ySize + uSize + vSize)
        }

        yuvBuffer!!.position(0)
        yBuffer.get(yuvBuffer!!.array(), 0, ySize)
        vBuffer.get(yuvBuffer!!.array(), ySize, vSize)
        uBuffer.get(yuvBuffer!!.array(), ySize + vSize, uSize)


        if (inputAllocation == null || width != image.width || height != image.height) {
            width = image.width
            height = image.height

            val yuvType = Type.Builder(rs, Element.U8(rs)).setX(yuvBuffer!!.array().size)
            inputAllocation = Allocation.createTyped(rs, yuvType.create(), Allocation.USAGE_SCRIPT)
            val rgbaType = Type.Builder(rs, Element.RGBA_8888(rs)).setX(width).setY(height)
            outputAllocation = Allocation.createTyped(rs, rgbaType.create(), Allocation.USAGE_SCRIPT)
        }

        inputAllocation!!.copyFrom(yuvBuffer!!.array())
        scriptYuvToRgb.setInput(inputAllocation)
        scriptYuvToRgb.forEach(outputAllocation)
        outputAllocation!!.copyTo(output)
    }
}