package com.job2day.jobsincanada.ui.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.job2day.jobsincanada.data.JobListing
import com.job2day.jobsincanada.service.BookmarkService
import com.job2day.jobsincanada.ui.components.CompanyLogo
import com.job2day.jobsincanada.ui.components.AdCardRow
import com.job2day.jobsincanada.ui.components.StatusBadge
import com.job2day.jobsincanada.ui.components.parseHexColor
import com.job2day.jobsincanada.ui.theme.Typography

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun JobDetailScreen(
    job: JobListing,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    var isSaved by remember { mutableStateOf(job.isSaved) }
    var isShareSheetOpen by remember { mutableStateOf(false) }

    // Color definitions based on category
    val (headerColor, tintColor) = when (job.category.lowercase()) {
        "engineering" -> Pair(Color(0xFFDBEAFE), Color(0xFF2563EB))
        "design" -> Pair(Color(0xFFF3E8FF), Color(0xFFA855F7))
        "marketing" -> Pair(Color(0xFFFEF3C7), Color(0xFFD97706))
        "product" -> Pair(Color(0xFFDCFCE7), Color(0xFF16A34A))
        "data" -> Pair(Color(0xFFFFEDD5), Color(0xFFEA580C))
        "finance" -> Pair(Color(0xFFF0FDF4), Color(0xFF15803D))
        "healthcare" -> Pair(Color(0xFFFFF1F2), Color(0xFFE11D48))
        else -> Pair(Color(0xFFE0F2FE), Color(0xFF0284C7))
    }

    val experienceRequired = when {
        job.title.lowercase().contains("senior") || job.title.lowercase().contains("staff") || job.title.lowercase().contains("lead") -> 5
        job.title.lowercase().contains("junior") || job.title.lowercase().contains("intern") -> 1
        else -> 3
    }

    val qualifications = when (job.category.lowercase()) {
        "engineering" -> listOf(
            "Bachelor's degree in Computer Science or related field",
            "$experienceRequired+ years of software engineering experience",
            "Strong proficiency in ${job.skills.take(2).joinToString(" and ")}",
            "Experience with Agile/Scrum development methodology",
            "Excellent problem-solving and communication skills"
        )
        "design" -> listOf(
            "Degree in Design, HCI, or equivalent practical experience",
            "$experienceRequired+ years of UX/product design experience",
            "Expert proficiency in Figma and design systems",
            "Strong portfolio demonstrating end-to-end design process",
            "Experience collaborating with cross-functional teams"
        )
        "data" -> listOf(
            "Bachelor's or Master's in Data Science, Statistics, or CS",
            "$experienceRequired+ years of data science or analytics experience",
            "Strong Python and SQL skills required",
            "Experience with ML frameworks (scikit-learn, TensorFlow)",
            "Ability to communicate insights to non-technical stakeholders"
        )
        "marketing" -> listOf(
            "Bachelor's degree in Marketing, Communications, or related",
            "$experienceRequired+ years of marketing experience",
            "Proven track record of driving brand awareness and growth",
            "Strong digital marketing and analytics skills",
            "Excellent written and verbal communication skills"
        )
        else -> listOf(
            "Relevant degree or equivalent work experience",
            "$experienceRequired+ years in a similar role",
            "Strong analytical and problem-solving abilities",
            "Excellent communication and interpersonal skills",
            "Ability to work in a fast-paced Canadian work environment"
        )
    }

    Scaffold { innerPadding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Main content scrollable column
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                // Header gradient area
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    headerColor,
                                    headerColor.copy(alpha = 0.5f),
                                    MaterialTheme.colorScheme.background
                                )
                            )
                        )
                ) {
                    if (job.isNew) {
                        Box(
                            modifier = Modifier
                                .statusBarsPadding()
                                .align(Alignment.TopEnd)
                                .padding(top = 16.dp, end = 16.dp)
                        ) {
                            StatusBadge(label = "NEW")
                        }
                    }
                }

                // Company Card & Title section (overlapping up into the header)
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .offset(y = (-50).dp)
                ) {
                    // Squircle container for logo with drop shadow
                    Box(
                        modifier = Modifier
                            .size(90.dp)
                            .shadow(6.dp, RoundedCornerShape(24.dp))
                            .clip(RoundedCornerShape(24.dp))
                            .background(Color.White)
                            .border(1.5.dp, Color.White, RoundedCornerShape(24.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        CompanyLogo(
                            companyName = job.company,
                            logoUrl = job.companyLogo,
                            size = 72.dp,
                            cornerRadius = 16.dp
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Job Title
                    Text(
                        text = job.title,
                        style = Typography.headlineSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center,
                        lineHeight = 32.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Company Name row with Verified badge
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = job.company,
                            style = Typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.Filled.Verified,
                            contentDescription = "Verified Employer",
                            tint = Color(0xFF16A34A),
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // Meta: Posted time and Category Badge
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = if (job.postedDaysAgo == 0) "Posted Today" else "Posted ${job.postedDaysAgo}d ago",
                            style = Typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Box(
                            modifier = Modifier
                                .size(4.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f))
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = job.category,
                            style = Typography.labelMedium,
                            color = tintColor,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Highlight Metrics Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        val metricItems = listOf(
                            Triple("Salary", job.salary, Icons.Outlined.AttachMoney),
                            Triple("Job Type", job.jobType, Icons.Outlined.WorkOutline),
                            Triple("Location", job.location.split(",").firstOrNull() ?: job.location, Icons.Outlined.LocationOn)
                        )

                        metricItems.forEach { (label, value, icon) ->
                            Card(
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                                shape = RoundedCornerShape(16.dp),
                                modifier = Modifier
                                    .weight(1f)
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 14.dp, horizontal = 4.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(36.dp)
                                            .clip(CircleShape)
                                            .background(headerColor),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = icon,
                                            contentDescription = null,
                                            tint = tintColor,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = label,
                                        style = Typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        fontSize = 11.sp
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = value,
                                        style = Typography.labelMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Premium Action Bar (Inline below metrics grid)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Bookmark Circular Button
                        IconButton(
                            onClick = {
                                BookmarkService.toggleSave(context, job.id)
                                isSaved = BookmarkService.isSaved(context, job.id)
                            },
                            modifier = Modifier
                                .size(54.dp)
                                .border(
                                    1.dp, 
                                    if (isSaved) tintColor else MaterialTheme.colorScheme.outlineVariant, 
                                    CircleShape
                                )
                                .background(
                                    if (isSaved) tintColor.copy(alpha = 0.08f) else Color.Transparent,
                                    CircleShape
                                )
                        ) {
                            Icon(
                                imageVector = if (isSaved) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                                contentDescription = "Save",
                                tint = if (isSaved) tintColor else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        
                        Spacer(modifier = Modifier.width(16.dp))
                        
                        // Gradient Apply Now Button
                        Button(
                            onClick = {
                                try {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(job.applyUrl))
                                    context.startActivity(intent)
                                } catch (e: Exception) {
                                    Toast.makeText(context, "Could not open job application link", Toast.LENGTH_SHORT).show()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                            contentPadding = PaddingValues(),
                            shape = RoundedCornerShape(100.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(54.dp)
                                .shadow(4.dp, RoundedCornerShape(100.dp))
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        Brush.horizontalGradient(
                                            colors = listOf(
                                                MaterialTheme.colorScheme.primary,
                                                Color(0xFF2D8A52)
                                            )
                                        ),
                                        RoundedCornerShape(100.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Apply Now",
                                    style = Typography.labelLarge,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Ad row containing 4 auto-resizing small card ads
                    AdCardRow(
                        placements = listOf("detail_1", "detail_2", "detail_3", "detail_4"),
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // Job Description Card
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(headerColor),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.Description,
                                        contentDescription = null,
                                        tint = tintColor,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = "Job Description",
                                    style = Typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                            
                            Spacer(modifier = Modifier.height(14.dp))
                            
                            MarkdownText(
                                markdown = job.description,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))


                    // Qualifications Card
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(headerColor),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.WorkspacePremium,
                                        contentDescription = null,
                                        tint = tintColor,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = "Qualifications",
                                    style = Typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            qualifications.forEach { qual ->
                                Row(
                                    verticalAlignment = Alignment.Top,
                                    modifier = Modifier.padding(bottom = 12.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .padding(top = 2.dp)
                                            .size(18.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xFFDCFCE7)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = null,
                                            tint = Color(0xFF16A34A),
                                            modifier = Modifier.size(10.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(
                                        text = qual,
                                        style = Typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        lineHeight = 22.sp,
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))


                    // Skills Chips Card
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp)
                        ) {
                            Text(
                                text = "Skills Required",
                                style = Typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                job.skills.forEach { skill ->
                                    Box(
                                        modifier = Modifier
                                            .clip(CircleShape)
                                            .background(headerColor.copy(alpha = 0.5f))
                                            .border(0.5.dp, tintColor.copy(alpha = 0.3f), CircleShape)
                                            .padding(horizontal = 14.dp, vertical = 6.dp)
                                    ) {
                                        Text(
                                            text = skill,
                                            style = Typography.labelLarge,
                                            color = tintColor,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Ad row containing 4 auto-resizing small card ads
                    AdCardRow(
                        placements = listOf("detail_5", "detail_6", "detail_7", "detail_8"),
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // About Company Card
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp)
                        ) {
                            Text(
                                text = "About Company",
                                style = Typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "Learn more about career growth, company values, and job openings at ${job.company}.",
                                style = Typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                lineHeight = 22.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Ad row containing 2 auto-resizing small card ads showing at the end
                    AdCardRow(
                        placements = listOf("detail_9", "detail_10"),
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    Spacer(modifier = Modifier.height(40.dp))
                }
            }

            // Transparent Floating Top Bar on top of all content
            TopAppBar(
                title = {},
                navigationIcon = {
                    Box(
                        modifier = Modifier
                            .padding(start = 12.dp)
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.8f))
                            .clickable { onBackClick() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color(0xFF111827),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { isShareSheetOpen = true },
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.8f))
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Share",
                            tint = Color(0xFF111827),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                ),
                windowInsets = WindowInsets(0.dp)
            )
        }
    }

    // Share Options Bottom Sheet
    if (isShareSheetOpen) {
        ModalBottomSheet(
            onDismissRequest = { isShareSheetOpen = false },
            dragHandle = { BottomSheetDefaults.DragHandle() },
            containerColor = Color.White
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 12.dp)
            ) {
                Text(
                    text = "Share Job",
                    style = Typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF111827)
                )
                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp)
                ) {
                    // Option 1: Copy Link
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable {
                            clipboardManager.setText(AnnotatedString(job.applyUrl))
                            Toast.makeText(context, "Link copied!", Toast.LENGTH_SHORT).show()
                            isShareSheetOpen = false
                        }
                    ) {
                        Box(
                            modifier = Modifier
                                .size(52.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surfaceVariant),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Link,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(text = "Copy Link", style = Typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    
                    // Option 2: Share via Intent (Email / general apps chooser)
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable {
                            val intent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_SUBJECT, "Job details for ${job.title} at ${job.company}")
                                putExtra(Intent.EXTRA_TEXT, "Hey! Check out this job: ${job.title} at ${job.company} - ${job.applyUrl}")
                            }
                            context.startActivity(Intent.createChooser(intent, "Share via"))
                            isShareSheetOpen = false
                        }
                    ) {
                        Box(
                            modifier = Modifier
                                .size(52.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surfaceVariant),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Share,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(text = "Share Info", style = Typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }
    }
}

@Composable
fun MarkdownText(
    markdown: String,
    modifier: Modifier = Modifier
) {
    val lines = markdown.split("\n")
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        lines.forEach { line ->
            val trimmed = line.trim()
            when {
                trimmed.startsWith("###") -> {
                    Text(
                        text = trimmed.removePrefix("###").trim(),
                        style = Typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(top = 10.dp, bottom = 4.dp)
                    )
                }
                trimmed.startsWith("##") -> {
                    Text(
                        text = trimmed.removePrefix("##").trim(),
                        style = Typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(top = 12.dp, bottom = 6.dp)
                    )
                }
                trimmed.startsWith("#") -> {
                    Text(
                        text = trimmed.removePrefix("#").trim(),
                        style = Typography.headlineSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(top = 14.dp, bottom = 8.dp)
                    )
                }
                trimmed.startsWith("-") || trimmed.startsWith("*") -> {
                    val bulletText = if (trimmed.startsWith("-")) trimmed.removePrefix("-").trim() else trimmed.removePrefix("*").trim()
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(start = 8.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(
                            text = "•",
                            style = Typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text(
                            text = parseBoldText(bulletText),
                            style = Typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            lineHeight = 22.sp
                        )
                    }
                }
                trimmed.isEmpty() -> {
                    Spacer(modifier = Modifier.height(4.dp))
                }
                else -> {
                    Text(
                        text = parseBoldText(trimmed),
                        style = Typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        lineHeight = 22.sp
                    )
                }
            }
        }
    }
}

fun parseBoldText(text: String): AnnotatedString {
    return buildAnnotatedString {
        val parts = text.split("**")
        parts.forEachIndexed { index, part ->
            if (index % 2 == 1) {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(part)
                }
            } else {
                append(part)
            }
        }
    }
}
