--MYSQL

delimiter //
CREATE PROCEDURE `subida_sal` (d INT, subida INT)
BEGIN
        UPDATE `empleados` SET salario = salario+subida WHERE dept_no=d;
END
//
delimiter ;





--MYSQL

delimiter //
CREATE PROCEDURE nombre_localidad_dep (d INT, OUT locali  VARCHAR(15), OUT nom VARCHAR(15))	
BEGIN
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET nom='INEXISTENTE';
	SELECT dnombre, loc INTO nom, locali FROM departamentos WHERE dept_no=d;

END
//
delimiter ;

