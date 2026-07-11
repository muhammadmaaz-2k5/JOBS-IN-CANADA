package com.job2day.jobsincanada.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.job2day.jobsincanada.data.Category
import com.job2day.jobsincanada.ui.theme.Typography

fun parseHexColor(hexString: String?, fallback: Color): Color {
    if (hexString.isNullOrEmpty()) return fallback
    return try {
        val clean = hexString.replace("#", "")
        if (clean.length == 6) {
            Color(android.graphics.Color.parseColor("#FF$clean"))
        } else {
            Color(android.graphics.Color.parseColor("#$clean"))
        }
    } catch (e: Exception) {
        fallback
    }
}

fun contrastColorFor(hexString: String?, fallback: Color): Color {
    if (hexString.isNullOrEmpty()) return fallback
    return when (hexString.replace("#", "").lowercase()) {
        "f3e8ff" -> Color(0xFF7C3AED)
        "fef3c7" -> Color(0xFFD97706)
        "dbeafe" -> Color(0xFF2563EB)
        "dcfce7" -> Color(0xFF15803D)
        "ffedd5" -> Color(0xFFEA580C)
        "f0fdf4" -> Color(0xFF16A34A)
        "fff1f2" -> Color(0xFFE11D48)
        "f8fafc" -> Color(0xFF475569)
        "eff6ff" -> Color(0xFF1D4ED8)
        "fff7ed" -> Color(0xFFC2410C)
        else -> fallback
    }
}

@Composable
fun CategoryGrid(
    categories: List<Category>,
    onCategoryTap: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        val rows = categories.chunked(5)
        for (row in rows) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                for (cat in row) {
                    val bgColor = parseHexColor(cat.color, Color(0xFFF3F4F6))
                    val iconColor = contrastColorFor(cat.color, MaterialTheme.colorScheme.primary)
                    
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .width(64.dp)
                            .clickable { onCategoryTap(cat.label) },
                        verticalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(52.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(bgColor),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = getIconByName(cat.icon),
                                contentDescription = cat.label,
                                tint = iconColor,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = cat.label,
                            style = Typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            textAlign = TextAlign.Center,
                            fontSize = 10.sp
                        )
                        Text(
                            text = cat.count.toString(),
                            style = Typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center,
                            fontSize = 9.sp
                        )
                    }
                }
            }
        }
    }
}
