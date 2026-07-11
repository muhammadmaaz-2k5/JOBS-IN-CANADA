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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.job2day.jobsincanada.data.JobListing
import com.job2day.jobsincanada.service.BookmarkService
import com.job2day.jobsincanada.ui.components.CompanyLogo
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

    val headerColor = when (job.category.lowercase()) {
        "engineering" -> Color(0xFFDBEAFE)
        "design" -> Color(0xFFF3E8FF)
        "marketing" -> Color(0xFFFEF3C7)
        "product" -> Color(0xFFDCFCE7)
        "data" -> Color(0xFFFFEDD5)
        "finance" -> Color(0xFFF0FDF4)
        "healthcare" -> Color(0xFFFFF1F2)
        else -> Color(0xFFE0F2FE)
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

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Job Details",
                        style = Typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF111827)
                    )
                },
                navigationIcon = {
                    Box(
                        modifier = Modifier
                            .padding(8.dp)
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .clickable { onBackClick() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { isShareSheetOpen = true }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Share",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        bottomBar = {
            // Bottom Sticky apply bar
            Surface(
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 20.dp, vertical = 14.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Bookmark Button
                    IconButton(
                        onClick = {
                            BookmarkService.toggleSave(context, job.id)
                            isSaved = BookmarkService.isSaved(context, job.id)
                        },
                        modifier = Modifier
                            .size(52.dp)
                            .border(1.dp, MaterialTheme.colorScheme.outline, CircleShape)
                    ) {
                        Icon(
                            imageVector = if (isSaved) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                            contentDescription = "Save",
                            tint = if (isSaved) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    
                    Spacer(modifier = Modifier.width(12.dp))
                    
                    // Apply Button
                    Button(
                        onClick = {
                            try {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(job.applyUrl))
                                context.startActivity(intent)
                            } catch (e: Exception) {
                                Toast.makeText(context, "Could not open job application link", Toast.LENGTH_SHORT).show()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        shape = RoundedCornerShape(100.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp)
                    ) {
                        Text(
                            text = "Apply Now",
                            style = Typography.labelLarge,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
        ) {
            // Header Image/Band Overlap
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(185.dp)
            ) {
                // Background color band
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                        .background(headerColor)
                ) {
                    if (job.isNew) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(16.dp)
                        ) {
                            StatusBadge(label = "NEW")
                        }
                    }
                }

                // Company Logo Overlapping (shifted down by bottom offset)
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .size(70.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .border(1.dp, Color.White, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    CompanyLogo(
                        companyName = job.company,
                        size = 54.dp,
                        cornerRadius = 27.dp
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Title & Company name
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = job.title,
                    style = Typography.headlineSmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF111827),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = job.company,
                    style = Typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Metrics row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                val metrics = listOf(
                    Triple("Salary", job.salary, Icons.Outlined.AttachMoney),
                    Triple("Job Time", job.jobType, Icons.Outlined.Schedule),
                    Triple("Location", job.location.split(",").firstOrNull() ?: job.location, Icons.Outlined.LocationOn)
                )
                
                metrics.forEach { (label, value, icon) ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .padding(vertical = 2.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp, horizontal = 4.dp)
                        ) {
                            Text(
                                text = label,
                                style = Typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = value,
                                style = Typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF111827),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Job Description Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(MaterialTheme.colorScheme.primaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Description,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Job Description",
                            style = Typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF111827)
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(14.dp))
                    
                    Text(
                        text = job.description,
                        style = Typography.bodyMedium,
                        color = Color(0xFF111827),
                        lineHeight = 22.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Qualifications Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(MaterialTheme.colorScheme.primaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.WorkspacePremium,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Qualifications",
                            style = Typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF111827)
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(14.dp))
                    
                    qualifications.forEach { qual ->
                        Row(
                            verticalAlignment = Alignment.Top,
                            modifier = Modifier.padding(bottom = 10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .padding(top = 2.dp)
                                    .size(18.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primaryContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(10.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = qual,
                                style = Typography.bodyMedium,
                                color = Color(0xFF111827),
                                lineHeight = 20.sp,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Skills Chips Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Skills Required",
                        style = Typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF111827)
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
                                    .background(MaterialTheme.colorScheme.surfaceVariant)
                                    .padding(horizontal = 14.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = skill,
                                    style = Typography.labelLarge,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // About Company Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "About Company",
                        style = Typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF111827)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Learn more about career growth, values, and openings at ${job.company}.",
                        style = Typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 20.sp
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Button(
                        onClick = {
                            try {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(job.applyUrl))
                                context.startActivity(intent)
                            } catch (e: Exception) {
                                Toast.makeText(context, "Could not open website", Toast.LENGTH_SHORT).show()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.primary
                        ),
                        shape = RoundedCornerShape(100.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Visit Employer Website",
                            style = Typography.labelLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))
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
