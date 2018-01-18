-- phpMyAdmin SQL Dump
-- version 4.7.4
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 18-01-2018 a las 14:04:22
-- Versión del servidor: 10.1.28-MariaDB
-- Versión de PHP: 7.1.11

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `hibernate`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `departamento`
--

CREATE TABLE `departamento` (
  `id` int(11) NOT NULL,
  `dnombre` varchar(20) DEFAULT NULL,
  `direccion` int(11) DEFAULT NULL,
  `jefe` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `departamento`
--

INSERT INTO `departamento` (`id`, `dnombre`, `direccion`, `jefe`) VALUES
(1, 'Programacion', 1, 1),
(2, 'Ventas', 2, NULL),
(3, 'Marketing', 4, NULL);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `direccion`
--

CREATE TABLE `direccion` (
  `id` int(11) NOT NULL,
  `calle` varchar(20) DEFAULT NULL,
  `numero` int(11) DEFAULT NULL,
  `cpostal` varchar(5) DEFAULT NULL,
  `provincia` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `direccion`
--

INSERT INTO `direccion` (`id`, `calle`, `numero`, `cpostal`, `provincia`) VALUES
(1, 'Mata', 1, '11111', 'Sevilla'),
(2, 'Mata', 2, '11111', 'Sevilla'),
(3, 'Ham', 30, '11111', 'Sevilla'),
(4, 'Mata', 3, '11111', 'Sevilla');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `empleado`
--

CREATE TABLE `empleado` (
  `id` int(11) NOT NULL,
  `nombre` varchar(20) NOT NULL,
  `apellido` varchar(20) NOT NULL,
  `salario` float DEFAULT NULL,
  `dep` int(11) DEFAULT NULL,
  `direccion` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `empleado`
--

INSERT INTO `empleado` (`id`, `nombre`, `apellido`, `salario`, `dep`, `direccion`) VALUES
(1, 'Pepe', 'Perez', 2000, 1, 3),
(2, 'Petra', 'Perez', 2000, 1, 3);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `informacion_financiera_departamento`
--

CREATE TABLE `informacion_financiera_departamento` (
  `departamento` int(11) NOT NULL,
  `presupuesto` float DEFAULT NULL,
  `ingresos` float DEFAULT NULL,
  `gastos` float DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `informacion_financiera_departamento`
--

INSERT INTO `informacion_financiera_departamento` (`departamento`, `presupuesto`, `ingresos`, `gastos`) VALUES
(3, 10000, 5000, 4000);

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `departamento`
--
ALTER TABLE `departamento`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `dnombre` (`dnombre`),
  ADD UNIQUE KEY `direccion` (`direccion`),
  ADD UNIQUE KEY `jefe` (`jefe`);

--
-- Indices de la tabla `direccion`
--
ALTER TABLE `direccion`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `empleado`
--
ALTER TABLE `empleado`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `nombre` (`nombre`,`apellido`);

--
-- Indices de la tabla `informacion_financiera_departamento`
--
ALTER TABLE `informacion_financiera_departamento`
  ADD PRIMARY KEY (`departamento`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `direccion`
--
ALTER TABLE `direccion`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT de la tabla `empleado`
--
ALTER TABLE `empleado`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
