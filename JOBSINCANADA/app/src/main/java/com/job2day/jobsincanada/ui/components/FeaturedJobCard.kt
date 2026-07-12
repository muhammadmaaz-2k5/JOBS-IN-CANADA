package com.job2day.jobsincanada.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowOutward
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.job2day.jobsincanada.data.JobListing
import com.job2day.jobsincanada.ui.theme.Typography

@Composable
fun FeaturedJobCard(
    job: JobListing,
    onTap: () -> Unit,
    onBookmarkToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isHighlighted = job.isFeatured && job.category.equals("Engineering", ignoreCase = true)
    
    val cardColor = if (isHighlighted) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.surface
    val textColor = if (isHighlighted) Color.White else MaterialTheme.colorScheme.onSurface
    val mutedTextColor = if (isHighlighted) Color.White.copy(alpha = 0.8f) else MaterialTheme.colorScheme.onSurfaceVariant
    val iconColor = if (isHighlighted) Color.White else MaterialTheme.colorScheme.primary

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
            .fillMaxWidth()
            .clickable { onTap() }
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Row 1: Company Logo + Name + Location + Bookmark
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                CompanyLogo(
                    companyName = job.company,
                    logoUrl = job.companyLogo,
                    size = 44.dp,
                    cornerRadius = 12.dp
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = job.company,
                        style = Typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = textColor,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.LocationOn,
                            contentDescription = null,
                            tint = mutedTextColor,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = job.location,
                            style = Typography.labelMedium,
                            color = mutedTextColor,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                
                IconButton(
                    onClick = onBookmarkToggle,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = if (job.isSaved) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                        contentDescription = "Save job",
                        tint = if (isHighlighted) Color.White else (if (job.isSaved) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline),
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Row 2: Salary Period
            Row(
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = job.salary,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = textColor
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "/ ${job.salaryPeriod}",
                    style = Typography.labelMedium,
                    color = mutedTextColor
                )
            }
            
            Spacer(modifier = Modifier.height(4.dp))
            
            // Row 3: Title
            Text(
                text = job.title,
                style = Typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = textColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            
            Spacer(modifier = Modifier.height(10.dp))
            
            // Row 4: Status Badges
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                StatusBadge(label = job.jobType)
                if (job.isRemote) {
                    StatusBadge(label = "Remote")
                }
                if (job.isNew) {
                    StatusBadge(label = "NEW")
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Row 5: Arrow Button
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.weight(1f))
                
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(if (isHighlighted) Color.White else MaterialTheme.colorScheme.onSurface),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowOutward,
                        contentDescription = "View details",
                        tint = if (isHighlighted) MaterialTheme.colorScheme.secondary else Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}
