package com.job2day.jobsincanada.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.job2day.jobsincanada.data.Category
import com.job2day.jobsincanada.ui.theme.Typography

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun FilterBottomSheet(
    currentFilters: Map<String, Any?>,
    categories: List<Category>,
    onApply: (Map<String, Any?>) -> Unit,
    onDismissRequest: () -> Unit,
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
) {
    val initialProvinces = (currentFilters["province"] as? List<*>)?.mapNotNull { it as? String } ?: emptyList()
    val initialJobTypes = (currentFilters["jobType"] as? List<*>)?.mapNotNull { it as? String } ?: emptyList()

    val selectedProvinces = remember { mutableStateListOf<String>().apply { addAll(initialProvinces) } }
    val selectedJobTypes = remember { mutableStateListOf<String>().apply { addAll(initialJobTypes) } }
    var remoteOnly by remember { mutableStateOf(currentFilters["remoteOnly"] as? Boolean ?: false) }
    var selectedCategory by remember { mutableStateOf(currentFilters["category"] as? String) }

    val jobTypes = listOf("Full-Time", "Part-Time", "Contract", "Internship", "Remote")
    val provinces = listOf(
        "Ontario",
        "British Columbia",
        "Quebec",
        "Alberta",
        "Manitoba",
        "Saskatchewan",
        "Nova Scotia",
        "New Brunswick",
        "Newfoundland",
        "Prince Edward Island"
    )

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Filter Jobs",
                    style = Typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF111827)
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "Clear All",
                    style = Typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable {
                        selectedProvinces.clear()
                        selectedJobTypes.clear()
                        remoteOnly = false
                        selectedCategory = null
                    }
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            Spacer(modifier = Modifier.height(16.dp))
            
            // Work Mode Remote Switch
            Text(
                text = "Work Mode",
                style = Typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF111827)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Remote Only",
                    style = Typography.bodyLarge,
                    color = Color(0xFF111827)
                )
                Spacer(modifier = Modifier.weight(1f))
                Switch(
                    checked = remoteOnly,
                    onCheckedChange = { remoteOnly = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = MaterialTheme.colorScheme.primary,
                        checkedTrackColor = MaterialTheme.colorScheme.primaryContainer
                    )
                )
            }
            
            Spacer(modifier = Modifier.height(20.dp))
            
            // Job Type Wrap
            Text(
                text = "Job Type",
                style = Typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF111827)
            )
            Spacer(modifier = Modifier.height(12.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                jobTypes.forEach { type ->
                    val selected = selectedJobTypes.contains(type)
                    val bgColor = if (selected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
                    val borderModifier = if (selected) Modifier.border(1.dp, MaterialTheme.colorScheme.primary, CircleShape) else Modifier
                    
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(bgColor)
                            .then(borderModifier)
                            .clickable {
                                if (selected) selectedJobTypes.remove(type) else selectedJobTypes.add(type)
                            }
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = type,
                            style = Typography.labelLarge,
                            fontWeight = FontWeight.Medium,
                            color = if (selected) MaterialTheme.colorScheme.primary else Color(0xFF111827)
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(20.dp))
            
            // Province Wrap
            Text(
                text = "Province",
                style = Typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF111827)
            )
            Spacer(modifier = Modifier.height(12.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                provinces.forEach { province ->
                    val selected = selectedProvinces.contains(province)
                    val bgColor = if (selected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
                    val borderModifier = if (selected) Modifier.border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(8.dp)) else Modifier
                    
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(bgColor)
                            .then(borderModifier)
                            .clickable {
                                if (selected) selectedProvinces.remove(province) else selectedProvinces.add(province)
                            }
                            .padding(horizontal = 12.dp, vertical = 7.dp)
                    ) {
                        Text(
                            text = province,
                            style = Typography.labelMedium,
                            fontWeight = FontWeight.Medium,
                            color = if (selected) MaterialTheme.colorScheme.primary else Color(0xFF111827)
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(20.dp))
            
            // Category Wrap
            if (categories.isNotEmpty()) {
                Text(
                    text = "Category",
                    style = Typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF111827)
                )
                Spacer(modifier = Modifier.height(12.dp))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    categories.forEach { cat ->
                        val selected = selectedCategory == cat.label
                        val bgColor = if (selected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
                        val borderModifier = if (selected) Modifier.border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(8.dp)) else Modifier
                        
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(bgColor)
                                .then(borderModifier)
                                .clickable {
                                    selectedCategory = if (selected) null else cat.label
                                }
                                .padding(horizontal = 12.dp, vertical = 7.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = getIconByName(cat.icon),
                                    contentDescription = null,
                                    tint = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = cat.label,
                                    style = Typography.labelMedium,
                                    fontWeight = FontWeight.Medium,
                                    color = if (selected) MaterialTheme.colorScheme.primary else Color(0xFF111827)
                                )
                            }
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Apply Button
            val activeFilterCount = selectedProvinces.size + selectedJobTypes.size + (if (remoteOnly) 1 else 0) + (if (selectedCategory != null) 1 else 0)
            Button(
                onClick = {
                    onApply(
                        mapOf(
                            "province" to selectedProvinces.toList(),
                            "jobType" to selectedJobTypes.toList(),
                            "remoteOnly" to remoteOnly,
                            "category" to selectedCategory
                        )
                    )
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                shape = RoundedCornerShape(100.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text(
                    text = "Apply Filters ($activeFilterCount)",
                    style = Typography.labelLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
