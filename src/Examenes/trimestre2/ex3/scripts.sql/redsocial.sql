-- phpMyAdmin SQL Dump
-- version 4.7.4
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 05-02-2018 a las 10:44:05
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
-- Base de datos: `redsocial`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `lider`
--

CREATE TABLE `lider` (
  `id` int(11) NOT NULL,
  `nombre` varchar(20) DEFAULT NULL,
  `fechaAlta` date DEFAULT NULL,
  `nMG` int(11) DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `lider`
--

INSERT INTO `lider` (`id`, `nombre`, `fechaAlta`, `nMG`) VALUES
(1, 'Adolfito', '2018-02-05', 2),
(2, 'KimJongNyan', '2018-02-05', 0);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `lid_tiene_seg`
--

CREATE TABLE `lid_tiene_seg` (
  `idLid` int(11) NOT NULL,
  `idSeg` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `lid_tiene_seg`
--

INSERT INTO `lid_tiene_seg` (`idLid`, `idSeg`) VALUES
(1, 1),
(1, 3),
(2, 1),
(2, 2);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `seguidor`
--

CREATE TABLE `seguidor` (
  `id` int(11) NOT NULL,
  `nick` varchar(20) DEFAULT NULL,
  `correo` varchar(40) DEFAULT NULL,
  `pais` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `seguidor`
--

INSERT INTO `seguidor` (`id`, `nick`, `correo`, `pais`) VALUES
(1, 'baguette', 'francois@gmail.com', 'Francia'),
(2, 'pepe', 'pepe.perez@gmail.com', 'Espaa'),
(3, 'felipe01', 'felipito@gmail.com', 'Espaa');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `lider`
--
ALTER TABLE `lider`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `nombre` (`nombre`);

--
-- Indices de la tabla `lid_tiene_seg`
--
ALTER TABLE `lid_tiene_seg`
  ADD PRIMARY KEY (`idLid`,`idSeg`);

--
-- Indices de la tabla `seguidor`
--
ALTER TABLE `seguidor`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `nick` (`nick`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `lider`
--
ALTER TABLE `lider`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT de la tabla `seguidor`
--
ALTER TABLE `seguidor`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
