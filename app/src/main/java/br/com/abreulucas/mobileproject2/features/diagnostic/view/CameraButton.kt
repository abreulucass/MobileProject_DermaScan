package br.com.abreulucas.mobileproject2.features.diagnostic.view


import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import br.com.abreulucas.mobileproject2.R

@Composable
fun CameraButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .clickable { onClick() }
            .padding(8.dp)
    ) {
        Image(
            painter = painterResource(id = android.R.drawable.ic_menu_camera),
            contentDescription = "Abrir câmera",
            modifier = Modifier.size(96.dp)
        )
        Text("Câmera", style = MaterialTheme.typography.bodyLarge)
    }
}