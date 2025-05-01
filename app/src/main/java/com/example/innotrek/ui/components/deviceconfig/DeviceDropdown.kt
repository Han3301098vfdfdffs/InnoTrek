package com.example.innotrek.ui.components.deviceconfig

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.example.innotrek.data.model.Device
import com.example.innotrek.ui.utils.composables.responsiveTextSize

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeviceDropdown(
    devices: List<Device>,
    selectedDeviceIndex: Int,
    onDeviceSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var dropdownExpanded by remember { mutableStateOf(false) }
    val sizeVerticalFont = 18.sp
    val sizeHorizontalFont = 26.sp


    ExposedDropdownMenuBox(
        expanded = dropdownExpanded,
        onExpandedChange = { dropdownExpanded = it },
        modifier = modifier
    ) {
        TextField(
            value = if (selectedDeviceIndex == -1) "Seleccionar dispositivo"
            else stringResource(id = devices[selectedDeviceIndex].deviceStringResourceId),
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = dropdownExpanded) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(type = ExposedDropdownMenuAnchorType.PrimaryEditable, enabled = true),
            textStyle = TextStyle(
                fontSize = responsiveTextSize(sizeVerticalFont, sizeHorizontalFont)
            )
        )

        ExposedDropdownMenu(
            expanded = dropdownExpanded,
            onDismissRequest = { dropdownExpanded = false }
        ) {
            DropdownMenuItem(
                text = {
                    Text(
                        text = "Seleccionar dispositivo",
                        fontSize = responsiveTextSize(sizeVerticalFont, sizeHorizontalFont)
                    )
                },
                onClick = {
                    onDeviceSelected(-1)
                    dropdownExpanded = false
                }
            )
            devices.forEachIndexed { index, device ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = stringResource(id = device.deviceStringResourceId),
                            fontSize = responsiveTextSize(sizeVerticalFont, sizeHorizontalFont)
                        )
                    },
                    onClick = {
                        onDeviceSelected(index)
                        dropdownExpanded = false
                    }
                )
            }
        }
    }
}