  function eliminarAuditoria(id) {
	console.log(id);
	swal({
		  title: "Esta seguro de Eliminar?",
		  text: "Una vez eliminado no se prodra restablecer!",
		  icon: "warning",
		  buttons: true,
		  dangerMode: true,
		})
		.then((OK) => {
		  if (OK) {
			  $.ajax({
				 url:"/auditoria/eliminar/"+id,
				 success: function(res) {
					console.log(res);
				},			
			  });
		    swal("Registro eliminado con exito", {
		      icon: "success",
		    }).then((ok)=>{
		    	if(ok){
		    		location.href="/auditoria";
		    	}
		    });
		  } 
		});
}

function eliminarEntrevista(id,codigo) {
	console.log(id);
	swal({
		  title: "Esta seguro de Eliminar?",
		  text: "Una vez eliminado no se prodra restablecer!",
		  icon: "warning",
		  buttons: true,
		  dangerMode: true,
		})
		.then((OK) => {
		  if (OK) {
			  $.ajax({
				 url:"/entrevista/eliminar/"+id+"/"+codigo,
				 success: function(res) {
					console.log(res);
				},			
			  });
		    swal("Registro eliminado con exito", {
		      icon: "success",
		    }).then((ok)=>{
		    	if(ok){
		    		location.href="/entrevista/"+codigo
		    	}
		    });
		  } 
		});
}


  function eliminarUsuario(id) {
	console.log(id);
	swal({
		  title: "Esta seguro de Eliminar?",
		  text: "Una vez eliminado no se prodra restablecer!",
		  icon: "warning",
		  buttons: true,
		  dangerMode: true,
		})
		.then((OK) => {
		  if (OK) {
			  $.ajax({
				 url:"/usuarios/eliminar/"+id,
				 success: function(res) {
					console.log(res);
				},			
			  });
		    swal("Registro eliminado con exito", {
		      icon: "success",
		    }).then((ok)=>{
		    	if(ok){
		    		location.href="/usuarios";
		    	}
		    });
		  } 
		});
}