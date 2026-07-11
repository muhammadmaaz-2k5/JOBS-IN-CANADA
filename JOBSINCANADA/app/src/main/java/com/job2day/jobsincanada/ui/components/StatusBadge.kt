package com.job2day.jobsincanada.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.job2day.jobsincanada.ui.theme.Typography

@Composable
fun StatusBadge(
    label: String,
    modifier: Modifier = Modifier
) {
    val (bg, fg) = when (label.lowercase()) {
        "full-time" -> Color(0xFFDCFCE7) to Color(0xFF16A34A)
        "part-time" -> Color(0xFFDBEAFE) to Color(0xFF2563EB)
        "contract" -> Color(0xFFFEF3C7) to Color(0xFFD97706)
        "remote" -> Color(0xFFF3E8FF) to Color(0xFFA855F7)
        "internship" -> Color(0xFFFFEDD5) to Color(0xFFEA580C)
        "new" -> Color(0xFFDCFCE7) to Color(0xFF16A34A)
        else -> Color(0xFFF3F4F6) to Color(0xFF6B7280)
    }

    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(bg)
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text = label.uppercase(),
            color = fg,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            style = Typography.labelSmall
        )
    }
}
